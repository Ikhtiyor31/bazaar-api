package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.UserDeviceDto
import com.strawberry.bazaarapi.user.repository.UserDeviceRepository
import org.springframework.stereotype.Service

@Service
class UserDeviceServiceImpl(
    private val userDeviceRepository: UserDeviceRepository
): UserDeviceService {

    override fun addUserDevice(user: User, userDeviceDto: UserDeviceDto): UserDeviceDto {
        val userDevice =
            userDeviceRepository.findByUserIdAndDeviceKey(user.id, userDeviceDto.deviceKey) ?: userDeviceDto.toEntity(user)
        val savedUserDevice = userDeviceRepository.save(userDevice)

        return savedUserDevice.toResponse()
    }
}