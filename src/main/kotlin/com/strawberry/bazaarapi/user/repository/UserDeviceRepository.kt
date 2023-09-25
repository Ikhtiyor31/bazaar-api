package com.strawberry.bazaarapi.user.repository

import com.strawberry.bazaarapi.user.domain.UserDevice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDeviceRepository : JpaRepository<UserDevice, Long> {
    fun findByUserIdAndDeviceKey(userId: Long, deviceKey: String?): UserDevice?
}