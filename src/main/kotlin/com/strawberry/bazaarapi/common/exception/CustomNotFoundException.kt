package com.strawberry.bazaarapi.common.exception

open class CustomNotFoundException(
    exception: ExceptionMessage,
    notFoundId: String = "",
    cause: Throwable? = null
) : RuntimeException(exception.message + notFoundId, cause) {
    val exceptionCode: String = exception.name

    constructor(exception: ExceptionMessage) : this(exception, "", null)
}