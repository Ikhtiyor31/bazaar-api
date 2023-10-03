package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.domain.UserDevice
import com.strawberry.bazaarapi.user.dto.UserDeviceDto
import com.strawberry.bazaarapi.user.dto.UserDeviceExistResponse
import com.strawberry.bazaarapi.user.repository.UserDeviceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserDeviceServiceImpl(
    private val userDeviceRepository: UserDeviceRepository
) : UserDeviceService {

    override fun createUserDevice(user: User, userDeviceDto: UserDeviceDto): UserDeviceDto {
        val existingUserDevice = userDeviceRepository.findByUserIdAndDeviceKey(user.id, userDeviceDto.deviceKey)

        return existingUserDevice?.updateUserDevice(userDeviceDto)?.toResponse()
            ?: createNewUserDevice(userDeviceDto.toEntity(user)).toResponse()
    }

    private fun createNewUserDevice(userDevice: UserDevice): UserDevice {
        deleteUserOldDevices(userDevice.user)
        return userDeviceRepository.save(userDevice)
    }

    private fun deleteUserOldDevices(user: User) {
        userDeviceRepository.findAllByUser(user).forEach(UserDevice::delete)
    }

    override fun deleteUserDevice(user: User) {
        deleteUserOldDevices(user)
    }

    @Transactional(readOnly = true)
    override fun findUserDevice(user: User, deviceKey: String): UserDeviceExistResponse {
        val isUserDeviceExist = userDeviceRepository.existsByUserIdAndDeviceKey(user.id, deviceKey)

        return UserDeviceExistResponse(
            isUserDeviceExist = isUserDeviceExist
        )
    }
}