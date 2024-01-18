package com.strawberry.bazaarapi.user.dto

import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.enums.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class UserAdapter (
    private val user: User
): UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val grantedAuthority: GrantedAuthority = SimpleGrantedAuthority("ROLE_" + user.role.name)
        return listOf(grantedAuthority)
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
       return user.email
    }

    override fun isAccountNonExpired(): Boolean {
        return user.enabled
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return isEnabled
    }

    fun getRole(): Role {
        return user.role
    }

    fun userInfo(): User {
        return user
    }
}