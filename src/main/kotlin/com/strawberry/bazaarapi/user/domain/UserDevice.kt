package com.strawberry.bazaarapi.user.domain

import com.strawberry.bazaarapi.common.entity.BaseEntity
import com.strawberry.bazaarapi.user.dto.UserDeviceDto
import com.strawberry.bazaarapi.user.enums.PlatformType
import org.hibernate.annotations.Where
import javax.persistence.*


@Entity
@Table(name = "user_device")
@Where(clause = "deleted=false")
data class UserDevice(

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "device_key", nullable = false)
    var deviceKey: String,

    @Column(name = "device_fcm_token", nullable = false)
    var deviceFcmToken: String,

    @Column(name = "device_ip_address", nullable = true)
    var deviceIpAddress: String = "",

    @Column(name = "platform_type", nullable = false)
    @Enumerated(EnumType.STRING)
    var platformType: PlatformType = PlatformType.WEB,

    @Column(name = "device_os_version", nullable = true)
    var deviceOsVersion: String = "",

    @Column(name = "app_version", nullable = false)
    var appVersion: String,
): BaseEntity() {
    fun toResponse() : UserDeviceDto {
        return UserDeviceDto(
            deviceKey = deviceKey,
            deviceFcmToken = deviceFcmToken,
            deviceIpAddress = deviceIpAddress,
            platformType = platformType,
            deviceOsVersion = deviceOsVersion,
            appVersion = appVersion
        )
    }

    fun updateUserDevice(userDeviceDto: UserDeviceDto): UserDevice {
        this.deviceFcmToken = userDeviceDto.deviceFcmToken
        this.deviceIpAddress = userDeviceDto.deviceIpAddress
        this.deviceOsVersion = userDeviceDto.deviceOsVersion
        this.appVersion = userDeviceDto.appVersion
        return this
    }
}
