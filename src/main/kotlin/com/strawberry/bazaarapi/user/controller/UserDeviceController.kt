package com.strawberry.bazaarapi.user.controller

import com.strawberry.bazaarapi.common.security.LoggedInUser
import com.strawberry.bazaarapi.common.security.RoleMapping
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.UserDeviceDto
import com.strawberry.bazaarapi.user.dto.UserDeviceExistResponse
import com.strawberry.bazaarapi.user.enums.Role
import com.strawberry.bazaarapi.user.service.UserDeviceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/user-device")
class UserDeviceController(
    private val userDeviceService: UserDeviceService
) {

    @RoleMapping(Role.USER)
    @PostMapping
    fun createUserDevice(
        @LoggedInUser user: User,
        @RequestBody userDeviceDto: UserDeviceDto
    ): ResponseEntity<UserDeviceDto> {
        return ResponseEntity.ok(userDeviceService.createUserDevice(user, userDeviceDto))
    }

    @RoleMapping(Role.USER)
    @DeleteMapping
    fun deletedUserDevice(@LoggedInUser user: User): ResponseEntity<String> {
        userDeviceService.deleteUserDevice(user)
        return ResponseEntity.ok("Ok")
    }

    @RoleMapping(Role.USER)
    @GetMapping
    fun isUserDeviceExist(
        @LoggedInUser user: User,
        @RequestParam deviceKey: String
    ): ResponseEntity<UserDeviceExistResponse> {
        return ResponseEntity.ok(userDeviceService.findUserDevice(user, deviceKey))
    }

}