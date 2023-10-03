package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.*

interface UserService {
    fun createUser(userSignupRequest: UserSignupRequest): UserSignupResponse
    fun verifyEmail(accountVerificationRequest: AccountVerificationRequest): AccountVerificationResponse
    fun signInUser(userLoginRequest: UserLoginRequest): UserLoginResponse
    fun updateUserRole(updateUserRoleDto: UpdateUserRoleDto): UpdateUserRoleDto
    fun forgotPassword(email: String): ForgotPasswordResponse
    fun resetPassword(passwordResetRequest: PasswordResetRequest): PasswordResetResponse
    fun deleteUserAccount(email: String): String
    fun resendVerificationCode(email: String): UserSignupResponse
    fun getUser(userId: Long): User
}