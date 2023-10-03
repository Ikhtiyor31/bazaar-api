package com.strawberry.bazaarapi.user.dto

data class ForgotPasswordResponse(
    val message: String,
    val verificationCode: Long
)
