package com.strawberry.bazaarapi.admin.service

import com.strawberry.bazaarapi.user.dto.UpdateUserRoleDto

interface AdminService {
    fun updateUserRole(updateUserRoleDto: UpdateUserRoleDto): UpdateUserRoleDto
}