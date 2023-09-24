package com.strawberry.bazaarapi.user

import com.strawberry.bazaarapi.email.EmailDetails
import com.strawberry.bazaarapi.email.repository.EmailConfirmationRepositoryImpl
import com.strawberry.bazaarapi.email.repository.EmailVerificationRepository
import com.strawberry.bazaarapi.email.service.EmailService
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.UserSignupRequest
import com.strawberry.bazaarapi.user.dto.UserSignupResponse
import com.strawberry.bazaarapi.user.repository.UserDeviceRepository
import com.strawberry.bazaarapi.user.repository.UserRepository
import com.strawberry.bazaarapi.user.repository.UserRepositorySupportImpl
import com.strawberry.bazaarapi.user.service.UserJwtTokenService
import com.strawberry.bazaarapi.user.service.UserServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {
    @InjectMocks
    private lateinit var userService: UserServiceImpl

    @Mock
    private lateinit var userJwtTokenService: UserJwtTokenService

    @Spy
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @Mock
    private lateinit var emailService: EmailService

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var userRepositorySupportImpl: UserRepositorySupportImpl

    @Mock
    private lateinit var emailVerificationRepository: EmailVerificationRepository

    @Mock
    private lateinit var emailConfirmationRepositoryImpl: EmailConfirmationRepositoryImpl

    @Mock
    private lateinit var userDeviceRepository: UserDeviceRepository

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `createUser with valid input should return UserSignupResponse`() {
        // Arrange
        val userSignupRequest = UserSignupRequest(
            email = "ikhtiyor@gmail.com",
            password = "password123"
        )
        val existingUser = null
        val authCode: Long = 1234
        val hashedPassword = "hashedPassword123"
        val user = User().apply {
            this.email = userSignupRequest.email
            this.passwordHashed = hashedPassword
        }
        val expectedResponse = UserSignupResponse(
            userId = user.id,
            message = "please verify your email",
            verificationCode = authCode
        )

        `when`(userRepository.findByEmail(userSignupRequest.email)).thenReturn(existingUser)
        `when`(passwordEncoder.encode(userSignupRequest.password)).thenReturn(hashedPassword)
        `when`(userRepository.save(user)).thenReturn(user)
        `when`(emailVerificationRepository.findTopByUserIdOrderByIdDesc(user.id)).thenReturn(null)
        doNothing().`when`(emailService).sendSimpleMail(EmailDetails("", "", ""))

        // Act
        val actualResponse = userService.createUser(userSignupRequest)
        // Assert
        assertEquals(expectedResponse.userId, actualResponse.userId)
        assertEquals(expectedResponse.message, actualResponse.message)
    }

    @Test
    fun `create user device info`() {

    }

    @Test
    fun `create user should throw exception if the user with given email already exists`() {
        val userEmail = "ikhtiyor@gmail.com"
        val userSignupRequest = UserSignupRequest(
            email = userEmail,
            password = "password123"
        )

        val createUser = User().apply {
            id = 1L
            email = userEmail
            passwordHashed = "password1234"
            enabled = true
        }
        given(userRepository.findByEmail(userEmail)).willReturn(createUser)
        userService.createUser(userSignupRequest)
        val existedUser = userRepository.findByEmail(userEmail)
        assertThat(existedUser?.enabled).isEqualTo(true)
        //val existedUser = userRepository.findByEmail(userEmail)
        //assertNotNull(existedUser)

        //assertEquals(existedUser?.enabled, true)



    }
}