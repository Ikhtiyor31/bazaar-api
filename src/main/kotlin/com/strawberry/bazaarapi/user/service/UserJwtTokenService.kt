package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.user.domain.UserToken
import com.strawberry.bazaarapi.user.dto.UserAdapter
import com.strawberry.bazaarapi.user.dto.RefreshTokenRequest
import com.strawberry.bazaarapi.user.dto.UserLoginResponse


interface UserJwtTokenService {
    fun generateUserAccessToken(userId: String): UserLoginResponse
    fun extractUsername(token: String): String
    fun isTokenValid(token: String, userAdapter: UserAdapter) : Boolean
    fun generateUserRefreshToken(refreshTokenRequest: RefreshTokenRequest) : UserLoginResponse
    fun getUserAccessToken(username: String) : UserToken?

}