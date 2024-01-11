package com.strawberry.bazaarapi.common.exception


open class ResourceNotFoundException(
    exception: ExceptionMessage,
    cause: Throwable? = null
) : RuntimeException(exception.message, cause) {
    val exceptionCode: String = exception.name

    constructor(exceptionMessage: ExceptionMessage) : this(exceptionMessage, null)
}