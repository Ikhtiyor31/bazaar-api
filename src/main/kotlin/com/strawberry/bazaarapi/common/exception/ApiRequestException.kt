package com.strawberry.bazaarapi.common.exception

import org.springframework.http.HttpStatus

data class ApiRequestException(
    val errorCode: ExceptionMessage = ExceptionMessage.INVALID_USERNAME_OR_PASSWORD,
    val messages: String = "",
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) : RuntimeException() {


    constructor(errorCode: ExceptionMessage) : this(
        errorCode = errorCode,
        messages = errorCode.message
    )

    constructor(errorCode: ExceptionMessage, httpStatus: HttpStatus) : this(
        errorCode = errorCode,
        messages = errorCode.message,
        httpStatus = httpStatus
    )
}
