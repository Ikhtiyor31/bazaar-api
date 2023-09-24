package com.strawberry.bazaarapi.user.domain

import com.strawberry.bazaarapi.user.enums.PlatformType
import javax.persistence.*


@Entity
@Table(name = "user_device")
data class UserDevice(

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    var user: User,

    @Column(name = "device_key", columnDefinition = "")
    var deviceKey: String,

    @Column(name = "device_fcm_token")
    var deviceFcmToken: String,

    @Column(name = "device_ip_address")
    var deviceIpAddress: String,

    @Column(name = "platform_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private var platformType: PlatformType? = PlatformType.WEB,

    @Column(name = "device_os_version")
    var deviceOsVersion: String,

    @Column(name = "app_version")
    var appVersion: String,
)
