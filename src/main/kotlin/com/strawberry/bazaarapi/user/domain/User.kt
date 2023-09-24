package com.strawberry.bazaarapi.user.domain


import com.fasterxml.jackson.annotation.JsonIgnore
import com.strawberry.bazaarapi.user.enums.Roles
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
@Table(name = "\"user\"")
data class User(

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name")
    var name: String = "",

    @Column(name = "email", unique = true)
    var email: String = "",

    @JsonIgnore
    @Column(name = "password")
    var passwordHashed: String = "",

    @Column(name = "address")
    var address: String = "",

    @Column(name = "profile_url")
    var profileUrl: String = "",

    @Column(name = "enabled", nullable = false, columnDefinition = "BIT default 0")
    var enabled: Boolean = false,

    @Column(name = "user_role")
    @Convert(converter = UserRoleConverter::class)
    var role: Roles = Roles.USER,

    @JsonIgnore
    @Column(name = "join_dt")
    var joinedAt: LocalDateTime? = getCurrentLocalTimeInUZT(),

    @JsonIgnore
    @Column(name = "last_login_dt")
    var lastLoggedAt: LocalDateTime? = getCurrentLocalTimeInUZT(),

    @Column(name = "deleted", nullable = false, columnDefinition = "BIT default 0")
    var deleted: Boolean? = false,

    ) : UserDetails {
    fun updateUserRole(userRole: String): User {
        return when (userRole) {
            "MANAGER" -> this.copy(role = Roles.MANAGER)
            "ADMIN" -> this.copy(role = Roles.ADMIN)
            else -> this.copy(role = Roles.USER)
        }
    }

    fun updateUserSignupStatus(enabled: Boolean) {
        this.enabled = enabled
    }

    fun updateUserPassword(passwordHashed: String) {
        this.passwordHashed = passwordHashed
    }

    fun updateLastLogin() {
        this.lastLoggedAt = LocalDateTime.now()
    }

    fun delete() {
        this.deleted = true
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return Collections.singletonList(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword(): String {
        return passwordHashed
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}
