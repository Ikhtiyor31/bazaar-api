package com.strawberry.bazaarapi.common.exception

import java.time.LocalDateTime

data class ApiExceptionResponse(
        val errorCode: ExceptionMessage = ExceptionMessage.INTERNAL_SERVER_ERROR,
        val message: String,
        val timestamp: LocalDateTime
)
