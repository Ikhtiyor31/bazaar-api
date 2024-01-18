package com.strawberry.bazaarapi.user.service


import com.strawberry.bazaarapi.user.domain.UserToken
import com.strawberry.bazaarapi.common.exception.ApiAuthenticationException
import com.strawberry.bazaarapi.common.exception.ExceptionMessage
import com.strawberry.bazaarapi.user.dto.UserAdapter
import com.strawberry.bazaarapi.user.repository.UserJwtAccessTokenRepository
import com.strawberry.bazaarapi.user.dto.RefreshTokenRequest
import com.strawberry.bazaarapi.user.dto.UserLoginResponse
import com.strawberry.bazaarapi.util.UserUtil.ACCESS_TOKEN_LIFETIME_HOUR
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserJwtTokenServiceImpl(
    private val userJwtAccessTokenRepository: UserJwtAccessTokenRepository
) : UserJwtTokenService {

    override fun generateUserAccessToken(userId: String): UserLoginResponse {

        val accessToken = generateAccessToken(userId)
        val refreshToken = generateRefreshToken(userId)

        val userAccessToken = UserToken(
            username = userId,
            accessToken = accessToken.first,
            lifetimeHour = ACCESS_TOKEN_LIFETIME_HOUR,
            expiryAt = accessToken.second,
            refreshToken = refreshToken.first
        )
        userJwtAccessTokenRepository.save(userAccessToken)

        return UserLoginResponse(
            userAccessToken.accessToken,
            userAccessToken.refreshToken
        )
    }

    override fun extractUsername(token: String): String {
        return extractClaims(token, Claims.SUBJECT).toString()
    }

    override fun isTokenValid(token: String, userAdapter: UserAdapter): Boolean {
        val username = extractUsername(token)

        return username == userAdapter.username && !isTokenExpired(token)

    }

    override fun generateUserRefreshToken(refreshTokenRequest: RefreshTokenRequest): UserLoginResponse {
        val userToken = userJwtAccessTokenRepository.findTopByUsernameOrderByIdDesc(refreshTokenRequest.username)
            ?: throw ApiAuthenticationException(ExceptionMessage.USER_NOT_EXIST)

        if (userToken.username != refreshTokenRequest.username && refreshTokenRequest.refreshToken != userToken.refreshToken)
            throw ApiAuthenticationException(ExceptionMessage.USER_NOT_EXIST)

        val accessToken = generateAccessToken(refreshTokenRequest.username)
        val newRefreshToken = generateRefreshToken(refreshTokenRequest.username)
        return UserLoginResponse(
            accessToken = accessToken.first,
            refreshToken = newRefreshToken.first
        )
    }

    override fun getUserAccessToken(username: String): UserToken? {
        return userJwtAccessTokenRepository.findTopByUsernameOrderByIdDesc(username)
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = extractExpirationDate(token) ?: return false
        return expiration.before(Date())
    }

    private fun extractExpirationDate(token: String): Date? {
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