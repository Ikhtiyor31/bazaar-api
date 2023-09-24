package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.common.exception.ApiRequestException
import com.strawberry.bazaarapi.common.exception.ExceptionMessage
import com.strawberry.bazaarapi.util.TimeUtil.getCurrentLocalTimeInUZT
import com.strawberry.bazaarapi.email.*
import com.strawberry.bazaarapi.email.repository.EmailVerificationRepository
import com.strawberry.bazaarapi.email.repository.EmailConfirmationRepositoryImpl
import com.strawberry.bazaarapi.email.service.EmailService
import com.strawberry.bazaarapi.user.domain.EmailVerificationCode
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.*
import com.strawberry.bazaarapi.user.repository.UserRepositorySupport
import com.strawberry.bazaarapi.user.repository.UserRepository
import com.strawberry.bazaarapi.util.UserUtil
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val userJwtTokenService: UserJwtTokenService,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
    private val userRepositorySupport: UserRepositorySupport,
    private val userRepository: UserRepository,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val emailConfirmationRepositoryImpl: EmailConfirmationRepositoryImpl
) : UserService {


    @Transactional(readOnly = false)
    override fun createUser(userSignupRequest: UserSignupRequest): UserSignupResponse {

        var existingUser = userRepository.findByEmail(userSignupRequest.email)

        val authCode = UserUtil.generateAuthorizationCode(FOUR_DIGIT_CODE)

        if (existingUser != null && existingUser.isEnabled) {
            throw ApiRequestException(ExceptionMessage.USER_ALREADY_SIGNUP, HttpStatus.BAD_REQUEST)
        }

        val isEmailVerificationCodeAlreadyExist = emailVerificationRepository.findTopByUserIdOrderByIdDesc(existingUser?.id)
        val currentTime = getCurrentLocalTimeInUZT()

        if (existingUser != null && isEmailVerificationCodeAlreadyExist != null && isEmailVerificationCodeAlreadyExist.expiresAt!!.isAfter(currentTime))
            throw ApiRequestException(ExceptionMessage.USER_ALREADY_SIGNUP)

        if (existingUser == null) {
            existingUser = User().apply {
                this.email = userSignupRequest.email
                this.passwordHashed = passwordEncoder.encode(userSignupRequest.password)
            }
            userRepository.save(existingUser)
        } else {
            val hashedPassword = passwordEncoder.encode(userSignupRequest.password)
            userRepositorySupport.updateUserPassword(hashedPassword, existingUser.id!!)
        }

        sendVerificationEmail(existingUser, authCode)

        return UserSignupResponse(
            userId = existingUser.id,
            message = "please verify your email",
            verificationCode = authCode
        )
    }

    private fun sendVerificationEmail(user: User, authCode: Long) {
        val emailVerificationCode = EmailVerificationCode().apply {
            this.confirmationCode = authCode
            this.expiresAt = getCurrentLocalTimeInUZT().plusMinutes(5)
            this.user = user
        }

        emailVerificationRepository.save(emailVerificationCode)
        emailService.sendSimpleMail(
            EmailDetails(user.email, "your verification code: $authCode", "Verify your email")
        )
    }

    @Transactional(readOnly = false)
    override fun verifyEmail(accountVerificationRequest: AccountVerificationRequest): AccountVerificationResponse {
        val user = userRepository.findByEmail(accountVerificationRequest.email)
            ?: throw ApiRequestException(ExceptionMessage.USER_NOT_SIGNUP_YET)


        val emailConfirmationCode = emailConfirmationRepositoryImpl.findConfirmationCodeByUserId(
            user.id!!,
            accountVerificationRequest.confirmationCode
        ) ?: throw ApiRequestException(ExceptionMessage.INVALID_AUTHORIZATION_NUMBER)

        if (emailConfirmationCode.confirmedAt != null) {
            throw ApiRequestException(ExceptionMessage.EMAIL_ALREADY_CONFIRMED, HttpStatus.BAD_REQUEST)
        }

        if (emailConfirmationCode.confirmationCode != accountVerificationRequest.confirmationCode) {
            throw ApiRequestException(ExceptionMessage.INVALID_AUTHORIZATION_NUMBER)
        }


        emailConfirmationCode.updateConfirmedAt(LocalDateTime.now())
        user.updateUserSignupStatus(true)
        return AccountVerificationResponse(
            isConfirmed = true,
            "your email is successfully confirmed!"
        )
    }

    @Transactional(readOnly = false)
    override fun signInUser(userLoginRequest: UserLoginRequest): UserLoginResponse {
        val user = userRepository.findByEmail(userLoginRequest.email)
            ?: throw ApiRequestException(ExceptionMessage.INVALID_USERNAME_OR_PASSWORD, HttpStatus.UNAUTHORIZED)

        if (!passwordEncoder.matches(userLoginRequest.password, user.passwordHashed))
            throw ApiRequestException(ExceptionMessage.INVALID_USERNAME_OR_PASSWORD, HttpStatus.UNAUTHORIZED)

        if (user.deleted == true)
            throw ApiRequestException(ExceptionMessage.INVALID_USERNAME_OR_PASSWORD, HttpStatus.UNAUTHORIZED)

        if (!user.enabled) {
            throw ApiRequestException(ExceptionMessage.EMAIL_NOT_CONFIRMED_YET, HttpStatus.UNAUTHORIZED)
        }

        user.updateLastLogin()

        return userJwtTokenService.generateUserAccessToken(userLoginRequest.email)
    }
    @Transactional(readOnly = false)
    override fun forgotPassword(email: String): UserSignupResponse {
        val user = userRepository.findByEmail(email)
            ?: throw ApiRequestException(ExceptionMessage.USER_NOT_EXIST)

        if (!user.isEnabled)
            throw ApiRequestException(ExceptionMessage.USER_NOT_EXIST, HttpStatus.OK)

        val authCode = UserUtil.generateAuthorizationCode(6)

        val emailVerificationCode = EmailVerificationCode().apply {
            this.confirmationCode = authCode
            this.expiresAt = LocalDateTime.now().plusMinutes(5)
            this.user = user
        }

        emailVerificationRepository.save(emailVerificationCode)

        sendVerificationEmail(user, authCode)
        return UserSignupResponse(
            message = "Please enter a verification code that we have sent to your email at $email",
            verificationCode = authCode
        )
    }

    @Transactional(readOnly = false)
    override fun updatePassword(passwordResetRequest: PasswordResetRequest): PasswordResetResponse {
        val user = userRepository.findByEmail(passwordResetRequest.email)
            ?: throw ApiRequestException(ExceptionMessage.USER_NOT_EXIST)

        if (!user.isEnabled)
            throw ApiRequestException(ExceptionMessage.USER_NOT_EXIST, HttpStatus.OK)
        if (passwordResetRequest.newPassword != passwordResetRequest.confirmNewPassword)
            throw ApiRequestException(ExceptionMessage.PASSWORD_MISMATCH, HttpStatus.OK)

        val hashedPassword = passwordEncoder.encode(passwordResetRequest.newPassword)

        user.updateUserPassword(hashedPassword)

        return PasswordResetResponse(HttpStatus.OK, "Your password is updated")
    }


    override fun updateUserRole(role: String): String {
        return "OK";
    }

    override fun getUsers(): List<User> {
        return userRepository.findAll()
    }

    override fun getUser(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow{ApiRequestException(ExceptionMessage.USER_NOT_EXIST, "can not find user id $userId", HttpStatus.NOT_FOUND)}
    }

    @Transactional
    override fun deleteUserAccount(userId: Long): Long {
        val user = userRepository.findById(userId)

        user.ifPresent(User::delete)

        return userId
    }

    override fun resendVerificationCode(email: String): UserSignupResponse {
        val user = userRepository.findByEmail(email)
            ?: throw ApiRequestException(ExceptionMessage.USER_NOT_EXIST)

        if (user.isEnabled)
            throw ApiRequestException(ExceptionMessage.USER_ALREADY_SIGNUP)

        val authCode = UserUtil.generateAuthorizationCode(6)
        sendVerificationEmail(user, authCode)

        return UserSignupResponse(
            userId = user.id,
            message = "please verify your email",
            verificationCode = authCode
        )

    }

    companion object {
        private const val FOUR_DIGIT_CODE: Long = 4
    }
}