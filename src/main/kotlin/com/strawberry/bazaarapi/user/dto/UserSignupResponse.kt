package com.strawberry.bazaarapi.user.dto


data class UserSignupResponse(
    val userId: Long,
    val message: String,
    val verificationCode: Long
)
