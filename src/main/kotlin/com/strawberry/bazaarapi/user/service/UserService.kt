package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.*

interface UserService {

    fun createUser(userSignupRequest: UserSignupRequest) : UserSignupResponse
    fun verifyEmail(accountVerificationRequest: AccountVerificationRequest): AccountVerificationResponse
    fun signInUser(userLoginRequest: UserLoginRequest): UserLoginResponse
    fun updateUserRole(role: String) : String
    fun forgotPassword(email: String): UserSignupResponse
    fun updatePassword(passwordResetRequest: PasswordResetRequest): PasswordResetResponse
    fun deleteUserAccount(userId: Long): Long
    fun resendVerificationCode(email: String): UserSignupResponse
    fun getUsers(): List<User>
    fun getUser(userId: Long): User
}