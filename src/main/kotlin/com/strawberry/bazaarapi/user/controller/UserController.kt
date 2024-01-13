package com.strawberry.bazaarapi.user.controller

import com.strawberry.bazaarapi.common.security.LoggedInUser
import com.strawberry.bazaarapi.common.security.RoleMapping
import com.strawberry.bazaarapi.common.validation.ValidationSequence
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.enums.Role
import com.strawberry.bazaarapi.user.dto.*
import com.strawberry.bazaarapi.user.service.UserJwtTokenService
import com.strawberry.bazaarapi.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userJwtTokenService: UserJwtTokenService,
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

    @PostMapping("/verify-email")
    fun verifyEmail(@RequestBody accountVerificationRequest: AccountVerificationRequest): ResponseEntity<AccountVerificationResponse> {
        return ResponseEntity.ok(userService.verifyEmail(accountVerificationRequest))
    }

    @RoleMapping(Role.USER)
    @PostMapping("/token/refresh")
    fun refreshToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<Any> {
        return ResponseEntity.ok(
            userJwtTokenService.generateUserRefreshToken(refreshTokenRequest)
        )
    }

    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestParam email: String): ResponseEntity<ForgotPasswordResponse> {
        return ResponseEntity.ok(userService.forgotPassword(email))
    }

    @PatchMapping("/reset-password")
    fun resetPassword(@RequestBody passwordResetRequest: PasswordResetRequest): ResponseEntity<PasswordResetResponse> {
        return ResponseEntity.ok(userService.resetPassword(passwordResetRequest))
    }

    @RoleMapping(Role.USER)
    @DeleteMapping("/delete-account")
    fun deleteUserAccount(@LoggedInUser user: User): ResponseEntity<String> {
        return ResponseEntity.ok(userService.deleteUserAccount(user.email))
    }

    @PostMapping("/resend-verification-code/{email}")
    fun resendVerificationCode(@PathVariable("email") email: String): ResponseEntity<UserSignupResponse> {
        return ResponseEntity.ok(userService.resendVerificationCode(email))
    }

    @RoleMapping(Role.USER)
    @GetMapping("/{userId}")
    fun getUserInfo(@PathVariable("userId") userId: Long): ResponseEntity<UserProfile> {
        return ResponseEntity.ok().body(userService.getUserInfo(userId))
    }
}