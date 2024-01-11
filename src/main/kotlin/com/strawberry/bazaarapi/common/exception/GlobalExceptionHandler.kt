package com.strawberry.bazaarapi.common.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest


@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger : Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(BadRequestException::class)
    fun handleUserApiException(
        request: HttpServletRequest,
        ex: BadRequestException
    ): ResponseEntity<ErrorResponse> {
        logger.warn(ex.exceptionCode, ex)
        val body = ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.message,
            LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(CustomNotFoundException::class)
    fun handeNotFoundException(ex: CustomNotFoundException): ResponseEntity<ErrorResponse> {
        logger.warn(ex.exceptionCode, ex)
        val body = ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.message,
            LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }

   @ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(ex: ForbiddenException, request: WebRequest): ResponseEntity<ErrorResponse> {
        logger.warn(ex.exceptionCode, ex)
        val body = ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            ex.message,
            LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException): ResponseEntity<ErrorResponse> {
        logger.warn(ex.exceptionCode, ex)
        val body = ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.message,
            LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }

    @ExceptionHandler(UnAuthorizedException::class)
    fun handleUnauthorizedException(ex: UnAuthorizedException): ResponseEntity<ErrorResponse> {
        logger.warn(ex.message, ex)
        val body = ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            ex.message,
            LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body)
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleException(request: HttpServletRequest, ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error(ExceptionMessage.INTERNAL_SERVER_ERROR.message, ex)
        val body = ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.message,
            LocalDateTime.now(),
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handlerIllegalArgumentException(ex: IllegalArgumentException) : ResponseEntity<ErrorResponse> {
        logger.warn(ExceptionMessage.ARGUMENT_NOT_VALIDATION_EXCEPTION.message, ex)
        val body = ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.message,
            LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<ErrorResponse> {
        logger.warn(ExceptionMessage.ILLEGAL_STATE_EXCEPTION.message, ex)
        val body = ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.message,
            LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.warn(ExceptionMessage.ARGUMENT_NOT_VALIDATION_EXCEPTION.message, ex)
        val body = ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.message,
            LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleHttpRequestMethodNotSupportedException(
        e: HttpRequestMethodNotSupportedException, request: HttpServletRequest?
    ): ResponseEntity<ErrorResponse> {
        logger.warn(e.message, request)
        val body = ErrorResponse(
            HttpStatus.METHOD_NOT_ALLOWED.value(),
            e.message,
            LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(body)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    protected fun handleNoHandlerFoundException(
        e: NoHandlerFoundException,
        request: HttpServletRequest?
    ): ResponseEntity<ErrorResponse> {
        logger.warn(e.message, request)
        val body = ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            e.message,
            LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }

}