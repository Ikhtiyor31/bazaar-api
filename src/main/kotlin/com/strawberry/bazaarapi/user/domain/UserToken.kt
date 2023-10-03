package com.strawberry.bazaarapi.user.domain

import com.strawberry.bazaarapi.util.UserUtil.ACCESS_TOKEN_LIFETIME_HOUR
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user_access_token")
data class UserToken(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(name = "username", nullable = false)
        var username: String,

        @Column(name = "access_token", nullable = false)
        var accessToken: String,

        @Column(name = "lifetime_hour", nullable = false)
        var lifetimeHour: Int = ACCESS_TOKEN_LIFETIME_HOUR,

        @Column(name = "expiry_at", nullable = false)
        var expiryAt: Date,

        @Column(name = "refresh_token", nullable = false)
        var refreshToken: String,

        @Column(name = "created_at")
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @Column(name = "updated_at")
        var updatedAt: LocalDateTime? = null

) {
        fun isExpired(): Boolean {
                return this.expiryAt.before(Date())
        }
}
