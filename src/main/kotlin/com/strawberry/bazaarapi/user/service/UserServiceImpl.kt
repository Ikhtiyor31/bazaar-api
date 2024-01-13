package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.common.exception.ApiAuthenticationException
import com.strawberry.bazaarapi.common.exception.ExceptionMessage
import com.strawberry.bazaarapi.common.exception.ResourceNotFoundException
import com.strawberry.bazaarapi.email.*
import com.strawberry.bazaarapi.email.repository.EmailConfirmationRepositoryImpl
import com.strawberry.bazaarapi.email.repository.EmailVerificationRepository
import com.strawberry.bazaarapi.email.service.EmailService
import com.strawberry.bazaarapi.user.domain.EmailVerificationCode
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.*
import com.strawberry.bazaarapi.user.repository.UserRepository
import com.strawberry.bazaarapi.user.repository.UserRepositorySupport
import com.strawberry.bazaarapi.util.TimeUtil.getCurrentLocalTimeInUZT
import com.strawberry.bazaarapi.util.UserUtil
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
@Transactional
class UserServiceImpl(
    private val userJwtTokenService: UserJwtTokenService,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
    private val userRepositorySupport: UserRepositorySupport,
    private val userRepository: UserRepository,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val emailConfirmationRepositoryImpl: EmailConfirmationRepositoryImpl
) : UserService {

    override fun createUser(userSignupRequest: UserSignupRequest): UserSignupResponse {
        var existingUser = userRepository.findByEmail(userSignupRequest.email)
        val authCode = UserUtil.generateAuthorizationCode(FOUR_DIGIT_CODE)

        if (existingUser != null && existingUser.enabled)
            throw ApiAuthenticationException(ExceptionMessage.USER_ALREADY_SIGNUP)

        val isEmailVerificationCodeAlreadyExist = emailVerificationRepository.findTopByUserIdOrderByIdDesc(existingUser?.id)
        val currentTime = getCurrentLocalTimeInUZT()

        if (existingUser != null && isEmailVerificationCodeAlreadyExist != null && isEmailVerificationCodeAlreadyExist.expiresAt!!.isAfter(currentTime))
            throw ApiAuthenticationException(ExceptionMessage.USER_ALREADY_SIGNUP)

        if (existingUser == null) {
            existingUser = User().apply {
                this.email = userSignupRequest.email
                this.password = passwordEncoder.encode(userSignupRequest.password)
            }
            userRepository.save(existingUser)
        } else {
            val hashedPassword = passwordEncoder.encode(userSignupRequest.password)
            userRepositorySupport.updateUserPassword(hashedPassword, existingUser.id)
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

    override fun verifyEmail(accountVerificationRequest: AccountVerificationRequest): AccountVerificationResponse {
        val user = userRepository.findByEmail(accountVerificationRequest.email)
            ?: throw ApiAuthenticationException(ExceptionMessage.USER_NOT_SIGNUP_YET)


        val emailConfirmationCode = emailConfirmationRepositoryImpl.findConfirmationCodeByUserId(
            user.id,
            accountVerificationRequest.confirmationCode
        ) ?: throw ApiAuthenticationException(ExceptionMessage.INVALID_AUTHORIZATION_NUMBER)

        if (emailConfirmationCode.confirmedAt != null) {
            throw ApiAuthenticationException(ExceptionMessage.EMAIL_ALREADY_CONFIRMED)
        }

        if (emailConfirmationCode.confirmationCode != accountVerificationRequest.confirmationCode) {
            throw ApiAuthenticationException(ExceptionMessage.INVALID_AUTHORIZATION_NUMBER)
        }


        emailConfirmationCode.updateConfirmedAt(LocalDateTime.now())
        user.updateUserSignupStatus(true)
        return AccountVerificationResponse(
            isConfirmed = true,
            "your email is successfully confirmed!"
        )
    }

    override fun signInUser(userLoginRequest: UserLoginRequest): UserLoginResponse {
        val user = userRepository.findByEmail(userLoginRequest.email)
            ?: throw ApiAuthenticationException(ExceptionMessage.INVALID_USERNAME_OR_PASSWORD)

        if (!passwordEncoder.matches(userLoginRequest.password, user.password))
            throw ApiAuthenticationException(ExceptionMessage.INVALID_USERNAME_OR_PASSWORD)

        if (user.deleted == true)
            throw ApiAuthenticationException(ExceptionMessage.INVALID_USERNAME_OR_PASSWORD)

        if (!user.enabled) {
            throw ApiAuthenticationException(ExceptionMessage.EMAIL_NOT_CONFIRMED_YET)
        }

        user.updateLastLogin()

        return userJwtTokenService.generateUserAccessToken(userLoginRequest.email)
    }

    override fun forgotPassword(email: String): ForgotPasswordResponse {
        val user = userRepository.findByEmail(email)
            ?: throw ApiAuthenticationException(ExceptionMessage.USER_NOT_EXIST)

        if (!user.enabled)
            throw ApiAuthenticationException(ExceptionMessage.USER_NOT_EXIST)

        val authCode = UserUtil.generateAuthorizationCode(4)

        val emailVerificationCode = EmailVerificationCode().apply {
            this.confirmationCode = authCode
            this.expiresAt = LocalDateTime.now().plusMinutes(5)
            this.user = user
        }

        emailVerificationRepository.save(emailVerificationCode)

        sendVerificationEmail(user, authCode)
        return ForgotPasswordResponse(
            message = "Please enter a verification code that we have sent to your email at $email",
            verificationCode = authCode
        )
    }

    override fun resetPassword(passwordResetRequest: PasswordResetRequest): PasswordResetResponse {
        val user = userRepository.findByEmail(passwordResetRequest.email)
            ?: throw ApiAuthenticationException(ExceptionMessage.USER_NOT_EXIST)

        if (!user.enabled)
            throw ApiAuthenticationException(ExceptionMessage.USER_NOT_EXIST)
        if (passwordResetRequest.newPassword != passwordResetRequest.confirmNewPassword)
            throw ApiAuthenticationException(ExceptionMessage.PASSWORD_MISMATCH)

        val hashedPassword = passwordEncoder.encode(passwordResetRequest.newPassword)

        user.updatePassword(hashedPassword)

        return PasswordResetResponse(HttpStatus.OK, "Your password is updated")
    }

    @Transactional(readOnly = true)
    override fun getUserInfo(userId: Long): UserProfile {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException(ExceptionMessage.USER_NOT_EXIST) }
        val userProfile = UserProfile(
            user.name,
            user.email,
            user.profileUrl,
            user.address
        )

        return userProfile
    }

    override fun deleteUserAccount(email: String): String {
        val user = userRepository.findByEmail(email) ?: throw ApiAuthenticationException(ExceptionMessage.USER_NOT_EXIST)

        user.delete()

        return "OK"
    }

    override fun resendVerificationCode(email: String): UserSignupResponse {
        val user = userRepository.findByEmail(email)
            ?: throw ApiAuthenticationException(ExceptionMessage.USER_NOT_EXIST)

        if (user.enabled)
            throw ApiAuthenticationException(ExceptionMessage.USER_ALREADY_SIGNUP)

        val authCode = UserUtil.generateAuthorizationCode(6)
        sendVerificationEmail(user, authCode)

        return UserSignupResponse(
            userId = user.id,
            message = "please verify your email",
            verificationCode = authCode
        )

    }

    override fun findUserByEmail(email: String): AuthenticatedUser {
        val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("user doesn't exist with the email: $email")

        return AuthenticatedUser(user)
    }

    companion object {
        private const val FOUR_DIGIT_CODE: Long = 4
    }
}