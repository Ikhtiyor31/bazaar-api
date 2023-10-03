package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.BaseBazaarApiIntegrationTest.Companion.getUser
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.domain.UserDevice
import com.strawberry.bazaarapi.user.dto.UserDeviceDto
import com.strawberry.bazaarapi.user.enums.PlatformType
import com.strawberry.bazaarapi.user.repository.UserDeviceRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.context.annotation.ComponentScan
import org.springframework.transaction.annotation.Transactional
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Transactional
@ComponentScan("com.strawberry.bazaarapi")
internal class UserDeviceServiceTest(
    @Mock private val userDeviceRepository: UserDeviceRepository

) {

    @InjectMocks private lateinit var userDeviceService: UserDeviceServiceImpl

    @Test
    fun shouldCreateUserDeviceOrUpdate() {
        val userDeviceDto = UserDeviceDto(
            deviceKey = "UiasdfaSD234e23asdfasdfASDFasfsdfs",
            deviceFcmToken = "eklasdjhfeKLfaklhjkldafsdasdfsd12l3kjLKJsdflk232",
            deviceIpAddress = "212.43.45.12",
            platformType = PlatformType.IOS,
            deviceOsVersion = "12",
            appVersion = "1.0.2"
        )

        val userDevice = userDeviceDto.toEntity(getUser())
        `when`(userDeviceRepository.findByUserIdAndDeviceKey(userDevice.user.id, userDeviceDto.deviceKey)).thenReturn(null)
        `when`(userDeviceRepository.save(userDevice)).thenReturn(userDevice)
        val createdUserDevice = userDeviceService.createUserDevice(userDevice.user, userDeviceDto)
        assertThat(createdUserDevice).isEqualTo(userDeviceDto.toEntity(userDevice.user).toResponse())
    }

    @Test
    fun shouldDeleteOldUserDevices() {
        val userDevice = getUserDevice(getUser())
        val userDevices = listOf(
            userDevice,
            userDevice,
            userDevice
        )

        given(userDeviceRepository.findAllByUser(getUser())).willReturn(userDevices)
        userDeviceService.deleteUserDevice(getUser())
        Mockito.verify(userDeviceRepository).findAllByUser(getUser())
        Mockito.verify(userDeviceRepository, Mockito.times(1)).findAllByUser(getUser())

    }

    @Test
    fun checkIfUserDeviceExist() {
        val userDevice = getUserDevice(getUser())
        given(userDeviceRepository.existsByUserIdAndDeviceKey(getUser().id, userDevice.deviceKey)).willReturn(TRUE)
        val userDeviceExist = userDeviceService.findUserDevice(getUser(), userDevice.deviceKey)

        assertThat(userDeviceExist.isUserDeviceExist).isEqualTo(TRUE)
    }

    @Test
    fun checkWhenUserDeviceNotExist() {
        val userDevice = getUserDevice(getUser())
        val fakeDeviceKey = "asdfadfafdsfasdffakeasf122"
        given(userDeviceRepository.existsByUserIdAndDeviceKey(getUser().id, userDevice.deviceKey)).willReturn(TRUE)
        val userDeviceExist = userDeviceService.findUserDevice(getUser(), fakeDeviceKey)

        assertThat(userDeviceExist.isUserDeviceExist).isEqualTo(FALSE)
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