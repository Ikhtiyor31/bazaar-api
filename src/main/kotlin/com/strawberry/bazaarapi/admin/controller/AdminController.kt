package com.strawberry.bazaarapi.admin.controller

import com.strawberry.bazaarapi.admin.service.AdminService
import com.strawberry.bazaarapi.common.exception.ExceptionMessage
import com.strawberry.bazaarapi.common.exception.ForbiddenException
import com.strawberry.bazaarapi.common.security.RoleMapping
import com.strawberry.bazaarapi.user.dto.UpdateUserRoleDto
import com.strawberry.bazaarapi.user.enums.Role
import com.strawberry.bazaarapi.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
class AdminController(
    private val adminService: AdminService
) {

    @RoleMapping(Role.ADMIN)
    @PatchMapping("/update-role")
    fun updateUserRole(@RequestBody updateUserRoleDto: UpdateUserRoleDto): ResponseEntity<UpdateUserRoleDto> {
        return ResponseEntity.ok(adminService.updateUserRole(updateUserRoleDto))
    }
}