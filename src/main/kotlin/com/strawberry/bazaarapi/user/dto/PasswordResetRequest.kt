package com.strawberry.bazaarapi.user.dto


data class PasswordResetRequest(
    val email: String,
    val newPassword: String,
    val confirmNewPassword: String,

)
