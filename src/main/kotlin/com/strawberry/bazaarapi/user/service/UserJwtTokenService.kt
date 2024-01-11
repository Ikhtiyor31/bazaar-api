package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.user.domain.UserToken
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.AuthenticatedUser
import com.strawberry.bazaarapi.user.dto.RefreshTokenRequest
import com.strawberry.bazaarapi.user.dto.UserLoginResponse
import org.springframework.stereotype.Service


interface UserJwtTokenService {
    fun generateUserAccessToken(userId: String): UserLoginResponse
    fun extractUsername(token: String): String
    fun isTokenValid(token: String, authenticatedUser: AuthenticatedUser) : Boolean
    fun generateUserRefreshToken(refreshTokenRequest: RefreshTokenRequest) : UserLoginResponse
    fun getUserAccessToken(username: String) : UserToken?

}