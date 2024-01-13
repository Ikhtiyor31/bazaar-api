package com.strawberry.bazaarapi.common.exception

open class UnAuthorizedException(
    exception: ExceptionMessage,
    throwable: Throwable? = null
) : RuntimeException(exception.message) {
    val exceptionCode: String = exception.name

    constructor(exception: ExceptionMessage) : this(exception, null)
}