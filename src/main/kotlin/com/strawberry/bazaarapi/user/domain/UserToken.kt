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
        /**
         * PK
         */
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(name = "username")
        var username: String = "",
        /**
         * access token
         * */
        @Column(name = "access_token")
        var accessToken: String? = null,

        @Column(name = "lifetime_hour")
        var lifetimeHour: Int = ACCESS_TOKEN_LIFETIME_HOUR,

        @Column(name = "expiry_at")
        var expiryAt: Date? = null,

        /**
         * additional token to refresh expired token
         */
        @Column(name = "refresh_token")
        var refreshToken: String? = null,

        /**
         * created date and time
         */
        @Column(name = "created_at")
        var createdAt: LocalDateTime = LocalDateTime.now(),

        /**
         * update date and time
         */
        @Column(name = "updated_at")
        var updatedAt: LocalDateTime? = null

) {
        fun isExpired(): Boolean {
                this.expiryAt ?: return false
                return this.expiryAt!!.before(Date())
        }
}
