package com.strawberry.bazaarapi.user.dto


data class UserSignupResponse(
    val userId: Long? = null,

    val message: String? = null,

    val verificationCode: Long? = null
)
