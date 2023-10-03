package com.strawberry.bazaarapi.user.dto

import com.strawberry.bazaarapi.user.enums.Role

data class UpdateUserRoleDto(
    val email: String,
    val userRole: Role
) {
    fun toResponse(username: String, userRole: Role): UpdateUserRoleDto {
        return UpdateUserRoleDto(
            username,
            userRole
        )
    }
}
