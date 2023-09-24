package com.strawberry.bazaarapi.user.dto

data class UserLoginResponse(

        val userAccessToken: String? = null,

        val userRefreshToken: String? = null
)