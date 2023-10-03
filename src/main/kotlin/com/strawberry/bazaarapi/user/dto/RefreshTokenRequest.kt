package com.strawberry.bazaarapi.user.dto


data class RefreshTokenRequest(
        val username: String,
        val refreshToken: String,
)