package com.strawberry.bazaarapi.common.exception

open class CustomNotFoundException(
    exception: ExceptionMessage,
    cause: Throwable? = null
) : RuntimeException(exception.message, cause) {
    val exceptionCode: String = exception.name

    constructor(exception: ExceptionMessage) : this(exception, null)
}