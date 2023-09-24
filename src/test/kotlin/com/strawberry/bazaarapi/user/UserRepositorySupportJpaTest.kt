package com.strawberry.bazaarapi.user

import com.strawberry.bazaarapi.common.config.QueryDslConfig
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.domain.UserDevice
import com.strawberry.bazaarapi.user.enums.PlatformType
import com.strawberry.bazaarapi.user.repository.UserDeviceRepository
import com.strawberry.bazaarapi.user.repository.UserRepository
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
@Import(QueryDslConfig::class)
internal class UserRepositorySupportJpaTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val userDeviceRepository: UserDeviceRepository,
){

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        userDeviceRepository.deleteAll()
    }


    @Test
    fun `check user is actually saved`() {
        val savedUser = userRepository.save(getUser())
        assertThat(savedUser).isNotEqualTo(null)
        assertThat(savedUser.email).isEqualTo(userEmail)
        assertThat(savedUser.name).isNotEqualTo(null)
        assertThat(savedUser.id).isNotEqualTo(null)
    }

    @Test
    fun `it should check if user email exists`() {
        // given
        val user = getUser()
        userRepository.save(user)
        val savedUser = userRepository.findByEmail(userEmail)
        assertThat(savedUser).isNotNull
        assertThat(user.email).isEqualTo(savedUser?.email)
    }

    
    @DisplayName("it should check if user email exists")
    @Test
    fun itShouldCheckIfUserEmailDoesNotExist() {
        // given
        val email = "abduazizov199531@mail.com"

        val savedUser = userRepository.findByEmail(email)
        assertThat(savedUser).isNull()
        assertThat(email).isNotEqualTo(savedUser?.email)
    }

    @Test
    fun `it should check if user email is duplicated`() {
        // Create a mock user object and save it to the database
        val mockUser = getUser()
        userRepository.save(mockUser)

        // Create another user object with the same email as the mock user object
        val duplicatedUser = User(
            name = "Duplicated User",
            email = userEmail,
            passwordHashed = "password"
        )

        // Call the findByEmail method with the email of the duplicated user object and assert that the result is not null
        val savedUser = userRepository.findByEmail(userEmail)
        assertThat(savedUser).isNotNull
        assertThat(savedUser?.email).isEqualTo(duplicatedUser.email)
        assertThrows<DataIntegrityViolationException> { userRepository.save(duplicatedUser) }
    }

    @Test
    fun `crate user device info`() {
        val user = getUser()
        userRepository.save(user)
        val savedUser = userRepository.findByEmail(userEmail)
        val device = UserDevice(
            user = savedUser!!,
            deviceKey = "device_key_1",
            deviceFcmToken = "fcm_token_1",
            deviceIpAddress = "192.168.1.1",
            platformType = PlatformType.ANDROID,
            deviceOsVersion = "Android 10",
            appVersion = "1.0.0"
        )

        userDeviceRepository.save(device)
        val userDeviceList = userDeviceRepository.findAll();

        assertThat(userDeviceList.size).isEqualTo(1)
    }




    companion object {
        private const val userEmail = "abduazizov199531@mail.com"
        private const val userPassword = "password1234$"
        private fun getUser(): User {
            return User().apply {
                this.name = "abdul"
                this.email = userEmail
                this.passwordHashed = userPassword
            }
        }
    }
}