package com.strawberry.bazaarapi.user.domain


import com.fasterxml.jackson.annotation.JsonIgnore
import com.strawberry.bazaarapi.user.enums.Role
import com.strawberry.bazaarapi.util.TimeUtil.getCurrentLocalTimeInUZT
import com.strawberry.bazaarapi.util.UserRoleConverter
import org.hibernate.annotations.DynamicUpdate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@DynamicUpdate
@EntityListeners(AuditingEntityListener::class)
@Table(name = "users")
data class User(

    @Id
    @Column(name = "user_id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(name = "name")
    var name: String = "",

    @Column(name = "email", unique = true)
    var email: String = "",

    @JsonIgnore
    @Column(name = "password")
    var password: String = "",

    @Column(name = "address")
    var address: String = "",

    @Column(name = "profile_url")
    var profileUrl: String = "",

    @Column(name = "enabled", nullable = false, columnDefinition = "BIT default 0")
    var enabled: Boolean = false,

    @Column(name = "user_role")
    @Convert(converter = UserRoleConverter::class)
    var role: Role = Role.USER,

    @JsonIgnore
    @Column(name = "join_dt")
    var joinedAt: LocalDateTime? = getCurrentLocalTimeInUZT(),

    @JsonIgnore
    @Column(name = "last_login_dt")
    var lastLoggedAt: LocalDateTime? = getCurrentLocalTimeInUZT(),

    @Column(name = "deleted", nullable = false, columnDefinition = "BIT default 0")
    var deleted: Boolean? = false,

    )  {
    fun updateUserRole(userRole: Role) {
        when (userRole) {
            Role.MANAGER -> this.role = Role.MANAGER
            Role.ADMIN -> this.role = Role.ADMIN
            else -> this.role = Role.USER
        }
    }

    fun updateUserSignupStatus(enabled: Boolean) {
        this.enabled = enabled
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun updateLastLogin() {
        this.lastLoggedAt = LocalDateTime.now()
    }

    fun delete() {
        this.deleted = true
    }

}
