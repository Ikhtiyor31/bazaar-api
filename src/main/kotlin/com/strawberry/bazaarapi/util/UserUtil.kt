package com.strawberry.bazaarapi.util

import java.security.SecureRandom
import java.util.*
import kotlin.math.pow


object UserUtil {
    const val REFRESH_TOKEN_LIFETIME_HOUR: Int = 5
    const val ACCESS_TOKEN_LIFETIME_HOUR: Int = 5
    private var rand: Random = SecureRandom.getInstanceStrong()
    private const val base = 10.0

    fun generateAuthorizationCode(length: Long): Long {
        val randomNumberOrigin = base.pow(length.toDouble()).toInt()
        val randomNumberBound = base.pow(length + 1.0).toInt()
        val authNumber: String = rand.ints(randomNumberOrigin, randomNumberBound)
            .findFirst()
            .orElseThrow { IllegalStateException("failed to create verification code") }.toString() + ""
        return authNumber.substring(0, length.toInt()).toLong()
    }

    fun isEmailValid(email: String): Boolean {
        val reg = Regex("^[0-9a-zA-Z_\\-.]*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
        return email.matches(reg)
    }
}
