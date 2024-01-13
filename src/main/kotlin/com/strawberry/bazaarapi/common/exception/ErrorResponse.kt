package com.strawberry.bazaarapi.common.exception

import java.time.LocalDateTime

data class ErrorResponse(
        val statusCode: Int,
        val message: String?,
        val timestamp: LocalDateTime
)
