package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.user.dto.*

interface UserService {
    fun createUser(userSignupRequest: UserSignupRequest): UserSignupResponse
    fun verifyEmail(accountVerificationRequest: AccountVerificationRequest): AccountVerificationResponse
    fun signInUser(userLoginRequest: UserLoginRequest): UserLoginResponse
    fun forgotPassword(email: String): ForgotPasswordResponse
    fun resetPassword(passwordResetRequest: PasswordResetRequest): PasswordResetResponse
    fun deleteUserAccount(email: String): String
    fun resendVerificationCode(email: String): UserSignupResponse
    fun findUserByEmail(email: String): AuthenticatedUser
    fun getUserInfo(userId: Long): UserProfile
}