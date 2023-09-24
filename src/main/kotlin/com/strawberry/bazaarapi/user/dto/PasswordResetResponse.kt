package com.strawberry.bazaarapi.user.dto

import org.springframework.http.HttpStatus

data class PasswordResetResponse(
    val statusCode: HttpStatus,
    val message: String
)
