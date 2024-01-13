package com.strawberry.bazaarapi.user.dto

data class UserLoginResponse(
    val accessToken: String,
    val refreshToken: String
)