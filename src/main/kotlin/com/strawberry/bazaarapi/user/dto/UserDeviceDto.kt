package com.strawberry.bazaarapi.user.dto

import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.domain.UserDevice
import com.strawberry.bazaarapi.user.enums.PlatformType

data class UserDeviceDto(
    val deviceKey: String,
    val deviceFcmToken: String,
    val deviceIpAddress: String,
    val platformType: PlatformType,
    val deviceOsVersion: String,
    val appVersion: String,
) {
    fun toEntity(user: User): UserDevice {
        return UserDevice(
            user = user,
            deviceKey = deviceKey,
            deviceFcmToken = deviceFcmToken,
            deviceIpAddress = deviceIpAddress,
            platformType = platformType,
            deviceOsVersion = deviceOsVersion,
            appVersion = appVersion
        )
    }
}

