package com.strawberry.bazaarapi.user.service

import com.strawberry.bazaarapi.user.domain.UserToken
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.UserLoginResponse
import org.springframework.stereotype.Service

@Service
interface UserJwtTokenService {
    fun generateUserAccessToken(userId: String): UserLoginResponse
    fun extractUsername(token: String): String
    fun isTokenValid(token: String, user: User?) : Boolean
    fun generateUserRefreshToken(username: String, refreshToken: String) : UserLoginResponse
    fun getUserAccessToken(username: String) : UserToken?

}