package com.strawberry.bazaarapi.user.controller

import com.strawberry.bazaarapi.common.security.LoggedInUser
import com.strawberry.bazaarapi.common.security.RoleMapping
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.UserDeviceDto
import com.strawberry.bazaarapi.user.enums.Roles
import com.strawberry.bazaarapi.user.service.UserDeviceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/user-device")
class UserDeviceController(
    private val userDeviceService: UserDeviceService
) {

    @RoleMapping(Roles.USER)
    @PostMapping
    fun createUserDeviceInfo(@LoggedInUser user: User, @RequestBody userDeviceDto: UserDeviceDto): ResponseEntity<UserDeviceDto> {
        return ResponseEntity.ok(userDeviceService.addUserDevice(user, userDeviceDto))
    }
}