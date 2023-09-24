package com.strawberry.bazaarapi.user.service


import com.strawberry.bazaarapi.user.domain.UserToken
import com.strawberry.bazaarapi.common.exception.ApiRequestException
import com.strawberry.bazaarapi.common.exception.ExceptionMessage
import com.strawberry.bazaarapi.user.repository.UserJwtAccessTokenRepository
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.dto.UserLoginResponse
import com.strawberry.bazaarapi.util.UserUtil.ACCESS_TOKEN_LIFETIME_HOUR
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserJwtTokenServiceImpl(
        @Autowired private val userJwtAccessTokenRepository: UserJwtAccessTokenRepository
): UserJwtTokenService {

    override fun generateUserAccessToken(userId: String): UserLoginResponse {

        val accessToken = generateAccessToken(userId)
        val refreshToken = generateRefreshToken(userId)

        val userAccessToken = UserToken().apply {
            this.accessToken = accessToken.first
            this.username = userId
            this.lifetimeHour = ACCESS_TOKEN_LIFETIME_HOUR
            this.expiryAt = accessToken.second
            this.refreshToken = refreshToken.first
        }
        userJwtAccessTokenRepository.save(userAccessToken)

        return UserLoginResponse(
                userAccessToken.accessToken,
                userAccessToken.refreshToken
        )
    }

    override fun extractUsername(token: String): String {
        return extractClaims(token, Claims.SUBJECT).toString()
    }

    override fun isTokenValid(token: String, user: User?) : Boolean {
        val username = extractUsername(token)
        if (user == null)
            return false

        return username == user.username && !isTokenExpired(token)

    }

    override fun generateUserRefreshToken(username: String, refreshToken: String): UserLoginResponse {
        val userToken = userJwtAccessTokenRepository.findByUsername(username) ?:
            throw ApiRequestException(ExceptionMessage.USER_NOT_EXIST, HttpStatus.OK)

        if (userToken.username != username && refreshToken != userToken.refreshToken)
            throw ApiRequestException(ExceptionMessage.USER_NOT_EXIST, HttpStatus.OK)

        val accessToken = generateAccessToken(username)
        val newRefreshToken = generateRefreshToken(username)
        return UserLoginResponse(
                userAccessToken = accessToken.first,
                userRefreshToken = newRefreshToken.first)
    }

    override fun getUserAccessToken(username: String): UserToken? {
        return userJwtAccessTokenRepository.findByUsername(username)
    }

    private fun isTokenExpired(token: String) : Boolean {
        val expiration = extractExpirationDate(token) ?: return false
        return expiration.before(Date())
    }

    private fun extractExpirationDate(token: String) : Date ? {
        val claims = extractAllClaims(token)
        return claims.expiration
    }

    private fun extractClaims(token: String, key: String): Any? {
        val claims = extractAllClaims(token)
        return claims[key]
    }


    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token).body
    }


    companion object {

        private const val ACCESS_TOKEN_EXPIRATION_TIME: Long = 1000 * 60 * 5
        private const val REFRESH_TOKEN_EXPIRATION_TIME: Long = 1000 * 60 * 15
        private val secretKey: ByteArray = "simple_secret".toByteArray()

        private fun generateAccessToken(username: String): Pair<String, Date> {
            val accessTokenExpTime = getAccessTokenExpTime()
            return Pair<String, Date>(
                Jwts.builder()
                    .setHeaderParam("type", "JWT")
                    .setSubject(username)
                    .setExpiration(accessTokenExpTime)
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact(), accessTokenExpTime
            )
        }

        private fun generateRefreshToken(username: String): Pair<String, Date> {
            val refreshTokenExpTime = getRefreshTokenExpTime()
            return Pair<String, Date>(
                Jwts.builder()
                    .setHeaderParam("type", "JWT")
                    .setSubject(username)
                    .setExpiration(refreshTokenExpTime)
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact(), refreshTokenExpTime
            )
        }

        private fun getAccessTokenExpTime(): Date {
            return Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME)
        }


        private fun getRefreshTokenExpTime(): Date {
            return Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME)
        }
    }

}