package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.UserDeviceDto
import com.strawberry.bazaarapi.user.dto.UserDeviceExistResponse

interface UserDeviceService {

    fun createUserDevice(user: User, userDeviceDto: UserDeviceDto): UserDeviceDto
    fun deleteUserDevice(user: User)
    fun findUserDevice(user: User, deviceKey: String): UserDeviceExistResponse
}