package com.strawberry.bazaarapi.common.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AuthorizationServiceException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionControllerAdvice {
    private val log = LoggerFactory.getLogger(ExceptionControllerAdvice::class.java)

    @ExceptionHandler(ApiRequestException::class)
    fun handleApiException(
        request: HttpServletRequest,
        ex: ApiRequestException
    ): ResponseEntity<ApiExceptionResponse> {
        val zonedDateTime = LocalDateTime.now()
        return ResponseEntity(
            ApiExceptionResponse(ex.errorCode, ex.messages, zonedDateTime),
            ex.httpStatus
        )
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleException(
        request: HttpServletRequest,
        ex: Exception
    ): ResponseEntity<ApiExceptionResponse> {
        return this.handleApiException(
            request,
            ApiRequestException(ExceptionMessage.INTERNAL_SERVER_ERROR, ex.message.toString(), HttpStatus.BAD_REQUEST)
        )
    }

    @ExceptionHandler(AuthorizationServiceException::class)
    fun unauthorizedException(request: HttpServletRequest, e: Exception): ResponseEntity<ApiExceptionResponse> {
        return this.handleApiException(request, ApiRequestException(
            ExceptionMessage.INTERNAL_SERVER_ERROR, "You are not authorized to do this operation", HttpStatus.FORBIDDEN))
    }
}