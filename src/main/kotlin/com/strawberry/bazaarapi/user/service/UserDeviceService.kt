package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.UserDeviceDto

interface UserDeviceService {

    fun addUserDevice(user: User, userDeviceDto: UserDeviceDto): UserDeviceDto
}