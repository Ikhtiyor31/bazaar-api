package com.strawberry.bazaarapi.common.exception

open class BadRequestException(
    exception: ExceptionMessage,
    causes: Throwable? = null
) : RuntimeException(exception.message, causes) {
    val exceptionCode: String = exception.name

    constructor(exception: ExceptionMessage) : this(exception, null)
}