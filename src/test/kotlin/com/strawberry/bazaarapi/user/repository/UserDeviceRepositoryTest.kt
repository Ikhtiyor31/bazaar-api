package com.strawberry.bazaarapi.user.repository

import com.strawberry.bazaarapi.common.config.QueryDslConfig
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.domain.UserDevice
import com.strawberry.bazaarapi.user.dto.UserDeviceDto
import com.strawberry.bazaarapi.user.enums.PlatformType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE


@DataJpaTest
@Transactional
@Import(QueryDslConfig::class)
@ComponentScan("com.strawberry.bazaarapi")
internal class UserDeviceRepositoryTest(
    @Autowired private val userDeviceRepository: UserDeviceRepository,
    @Qualifier("mockUser") private val mockUser: User
) {

    @BeforeEach
    fun setUp() {
        userDeviceRepository.deleteAll()
    }


    @Test
    fun shouldSaveUserDevice() {
        val userDevice = UserDevice(
            user = mockUser,
            deviceKey = "asdfasfafafasdfah345yww234",
            deviceFcmToken = "asdfasfsdagasgadsgaf",
            deviceIpAddress = "192.168.1.1",
            platformType = PlatformType.ANDROID,
            deviceOsVersion = "Android 10",
            appVersion = "1.0.0"
        )

        userDeviceRepository.save(userDevice)
        val userDeviceList = userDeviceRepository.findAll();

        assertThat(userDeviceList.size).isEqualTo(1)
    }

    @Test
    fun shouldUpdateUserDevice() {
        val userDevice = UserDevice(
            user = mockUser,
            deviceKey = "asfdafasf2342sgsdg",
            deviceFcmToken = "asdga324asdgherq324",
            deviceIpAddress = "292.168.1.14",
            platformType = PlatformType.ANDROID,
            deviceOsVersion = "Android 10",
            appVersion = "1.0.0"
        )
        userDeviceRepository.save(userDevice)
        val savedUserDevice = userDeviceRepository.findByUserIdAndDeviceKey(userDevice.user.id, userDevice.deviceKey)
        val userDeviceDto = UserDeviceDto(
            deviceKey = "UiasdfaSD234e23asdfasdfASDFasfsdfs",
            deviceFcmToken = "eklasdjhfeKLfaklhjkldafsdasdfsd12l3kjLKJsdflk232",
            deviceIpAddress = "212.43.45.12",
            platformType = PlatformType.IOS,
            deviceOsVersion = "12",
            appVersion = "1.0.2"
        )

        savedUserDevice!!.updateUserDevice(userDeviceDto)
        val updatedUserDevice =
            userDeviceRepository.findByUserIdAndDeviceKey(savedUserDevice.user.id, savedUserDevice.deviceKey)

        assertThat(updatedUserDevice!!.deviceFcmToken).isEqualTo(userDeviceDto.deviceFcmToken)
        assertThat(updatedUserDevice.deviceIpAddress).isEqualTo(userDeviceDto.deviceIpAddress)
        assertThat(updatedUserDevice.deviceOsVersion).isEqualTo(userDeviceDto.deviceOsVersion)
        assertThat(updatedUserDevice.appVersion).isEqualTo(userDeviceDto.appVersion)

        // not updated device properties
        assertThat(updatedUserDevice.deviceKey).isNotEqualTo(userDeviceDto.deviceKey)
        assertThat(updatedUserDevice.platformType).isNotEqualTo(userDeviceDto.platformType)
    }

    @Test
    fun shouldFindUserDevicesByUserIdAndDeviceKey() {
        val userDevice = UserDevice(
            user = mockUser,
            deviceKey = "UiasdfaSD234e23asdfasdfASDFasfsdfs",
            deviceFcmToken = "eklasdjhfeKLfaklhjkldafsdasdfsd12l3kjLKJsdflk232",
            deviceIpAddress = "112.43.45.10",
            platformType = PlatformType.IOS,
            deviceOsVersion = "IOS 13",
            appVersion = "0.07"
        )
        userDeviceRepository.save(userDevice)

        val userid = mockUser.id
        val deviceKey = userDevice.deviceKey

        val foundUserDevice = userDeviceRepository.findByUserIdAndDeviceKey(userid, deviceKey)
        assertThat(foundUserDevice).isEqualTo(userDevice)
        assertThat(foundUserDevice!!.deviceIpAddress).isEqualTo(userDevice.deviceIpAddress)
    }

    @Test
    fun shouldFindAllDevicesByUser() {
        val userDevice1 = getUserDevice(mockUser)
        val userDevice2 = getUserDevice(mockUser)
        val userDevice3 = getUserDevice(mockUser)
        userDeviceRepository.save(userDevice1)
        userDeviceRepository.save(userDevice2)
        userDeviceRepository.save(userDevice3)
        val userDevices: List<UserDevice> = userDeviceRepository.findAllByUser(mockUser)
        assertThat(userDevices.size).isEqualTo(3)
    }

    @Test
    fun shouldDeleteUserDevice() {
        val userDevice1 = getUserDevice(mockUser)
        val userDevice2 = getUserDevice(mockUser)
        userDeviceRepository.save(userDevice1)
        userDeviceRepository.save(userDevice2)

        //before
        var userDevices: List<UserDevice> = userDeviceRepository.findAllByUser(mockUser)
        assertThat(userDevices.size).isEqualTo(2)

        //after
        userDeviceRepository.findAllByUser(mockUser).forEach(UserDevice::delete)
        userDevices = userDeviceRepository.findAllByUser(mockUser)
        assertThat(userDevices.size).isEqualTo(0)
    }

    @Test
    fun shouldCheckWhenUserDeviceExist() {
        val userDevice = getUserDevice(mockUser)
        userDeviceRepository.save(userDevice)
        val isUserDeviceExist = userDeviceRepository.existsByUserIdAndDeviceKey(mockUser.id, userDevice.deviceKey)
        assertThat(isUserDeviceExist).isEqualTo(TRUE)
    }

    @Test
    fun shouldCheckWhenUserDeviceDeleted() {
        val userDevice = getUserDevice(mockUser)
        userDeviceRepository.save(userDevice)
        userDeviceRepository.findAllByUser(mockUser).forEach(UserDevice::delete)
        val isUserDeviceExist = userDeviceRepository.existsByUserIdAndDeviceKey(mockUser.id, userDevice.deviceKey)
        assertThat(isUserDeviceExist).isEqualTo(FALSE)
    }

    companion object {
        private fun getUserDevice(user: User) = UserDevice(
            user = user,
            deviceKey = "aslkdfjhl234o23as",
            deviceFcmToken = "alskjhdf&^9424asdfkjhsadfafkuweasfd",
            deviceIpAddress = "112.43.45.10",
            platformType = PlatformType.IOS,
            deviceOsVersion = "IOS 13",
            appVersion = "2.07"
        )
    }
}