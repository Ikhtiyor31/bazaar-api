package com.strawberry.bazaarapi.user.controller

import com.strawberry.bazaarapi.common.security.RoleMapping
import com.strawberry.bazaarapi.common.validation.ValidationSequence
import com.strawberry.bazaarapi.user.enums.Roles
import com.strawberry.bazaarapi.user.dto.*
import com.strawberry.bazaarapi.user.service.UserJwtTokenService
import com.strawberry.bazaarapi.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    @Autowired private val userJwtTokenService: UserJwtTokenService,
    val userService: UserService
) {

    @PostMapping("/signup")
    fun signUp(@RequestBody @Validated(ValidationSequence::class) userRequest: UserSignupRequest): ResponseEntity<UserSignupResponse> {
        return ResponseEntity.ok(userService.createUser(userRequest))
    }

    @PostMapping("/signin")
    fun signIn(@RequestBody @Validated userLoginRequest: UserLoginRequest): ResponseEntity<UserLoginResponse> {
        return ResponseEntity.ok(userService.signInUser(userLoginRequest))
    }

    @PostMapping("/confirm-email")
    fun verifyEmail(@RequestBody accountVerificationRequest: AccountVerificationRequest): ResponseEntity<AccountVerificationResponse> {
        return ResponseEntity.ok(userService.verifyEmail(accountVerificationRequest))
    }

    @RoleMapping(Roles.ADMIN)
    @PutMapping("/update-role")
    fun updateUserRole(@RequestBody role: String): ResponseEntity<String> {
        return ResponseEntity.ok(userService.updateUserRole(role))
    }

    @PostMapping("/token/refresh")
    fun refreshToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<Any> {
        return ResponseEntity.ok(
            userJwtTokenService.generateUserRefreshToken(refreshTokenRequest.username, refreshTokenRequest.refreshToken)
        )
    }

    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestBody userEmailRequest: UserEmailRequest): ResponseEntity<UserSignupResponse> {
        return ResponseEntity.ok(userService.forgotPassword(userEmailRequest.email))
    }

    @PutMapping("/reset-password")
    fun updatePassword(@RequestBody passwordResetRequest: PasswordResetRequest): ResponseEntity<PasswordResetResponse> {
        return ResponseEntity.ok(userService.updatePassword(passwordResetRequest))
    }

    @RoleMapping(Roles.USER)
    @DeleteMapping("/{userId}")
    fun deleteUserAccount(@PathVariable("userId") userId: Long): ResponseEntity<Long> {
        return ResponseEntity.ok(userService.deleteUserAccount(userId))
    }

    @PostMapping("/resend-verification-code/{email}")
    fun resendVerificationCode(@PathVariable("email") email: String): ResponseEntity<UserSignupResponse> {
        return ResponseEntity.ok(userService.resendVerificationCode(email))
    }

    @GetMapping("/users")
    fun getUsers(): ResponseEntity<Any> {
        return ResponseEntity.ok().body(userService.getUsers())
    }

    @GetMapping("/users/{userId}")
    fun getUsers(@PathVariable("userId") userId: Long): ResponseEntity<Any> {
        return ResponseEntity.ok().body(userService.getUser(userId))
    }
}