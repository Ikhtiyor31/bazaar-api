package com.strawberry.bazaarapi.user.controller

import com.strawberry.bazaarapi.common.security.LoggedInUser
import com.strawberry.bazaarapi.common.security.RoleMapping
import com.strawberry.bazaarapi.user.dto.UserAdapter
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
            @LoggedInUser userAdapter: UserAdapter,
            @RequestBody userDeviceDto: UserDeviceDto
    ): ResponseEntity<UserDeviceDto> {
        return ResponseEntity.ok(userDeviceService.createUserDevice(userAdapter.userInfo(), userDeviceDto))
    }

    @RoleMapping(Role.USER)
    @DeleteMapping
    fun deletedUserDevice(@LoggedInUser userAdapter: UserAdapter): ResponseEntity<String> {
        userDeviceService.deleteUserDevice(userAdapter.userInfo())
        return ResponseEntity.ok("Ok")
    }

    @RoleMapping(Role.USER)
    @GetMapping
    fun isUserDeviceExist(
            @LoggedInUser userAdapter: UserAdapter,
            @RequestParam deviceKey: String
    ): ResponseEntity<UserDeviceExistResponse> {
        return ResponseEntity.ok(userDeviceService.findUserDevice(userAdapter.userInfo(), deviceKey))
    }

}