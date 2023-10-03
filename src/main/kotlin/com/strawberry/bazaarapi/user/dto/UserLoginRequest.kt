package com.strawberry.bazaarapi.user.dto


data class UserLoginRequest(
        val email: String,
        val password: String,
)
