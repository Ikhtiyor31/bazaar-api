package com.strawberry.bazaarapi.common.security

import com.strawberry.bazaarapi.common.exception.ApiRequestException
import com.strawberry.bazaarapi.common.exception.ExceptionMessage
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.enums.Role
import com.strawberry.bazaarapi.user.service.UserJwtTokenService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*


@Aspect
@Component
class AuthAspect(
        private val userJwtTokenService: UserJwtTokenService
){

    @Around("@annotation(roleMapping)")
    fun aroundRoleMapping(joinPoint: ProceedingJoinPoint, roleMapping: RoleMapping): Any? {
        val user = SecurityContextHolder.getContext().authentication.principal as User

        val userAccessToken = userJwtTokenService.getUserAccessToken(user.username) ?:
            throw ApiRequestException(ExceptionMessage.INVALID_OR_EXPIRED_USER_ACCESS_TOKEN, HttpStatus.FORBIDDEN)

        if (userAccessToken.expiryAt.before(Date())) {
            throw ApiRequestException(ExceptionMessage.INVALID_OR_EXPIRED_USER_ACCESS_TOKEN, HttpStatus.FORBIDDEN)
        }

        if (!roleMapping.value.contains(user.role) || user.role != Role.ADMIN) {
            throw ApiRequestException(ExceptionMessage.USER_ACCESS_RESTRICTION, HttpStatus.FORBIDDEN)
        }

        return joinPoint.proceed()
    }
}