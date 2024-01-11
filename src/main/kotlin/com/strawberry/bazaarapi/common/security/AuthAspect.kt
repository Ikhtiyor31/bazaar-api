package com.strawberry.bazaarapi.common.security

import com.strawberry.bazaarapi.common.exception.ExceptionMessage
import com.strawberry.bazaarapi.common.exception.ForbiddenException
import com.strawberry.bazaarapi.common.exception.ApiAuthenticationException
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.enums.Role
import com.strawberry.bazaarapi.user.service.UserJwtTokenService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
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
        val user = SecurityContextHolder.getContext().authentication.principal as? User
            ?: throw ApiAuthenticationException(ExceptionMessage.USER_NOT_EXIST)

        val userAccessToken = userJwtTokenService.getUserAccessToken(user.email) ?:
            throw ForbiddenException(ExceptionMessage.INVALID_OR_EXPIRED_USER_ACCESS_TOKEN)

        if (userAccessToken.expiryAt.before(Date())) {
            throw ForbiddenException(ExceptionMessage.INVALID_OR_EXPIRED_USER_ACCESS_TOKEN)
        }

        if (!roleMapping.value.contains(user.role) && !roleMapping.value.contains(Role.ADMIN)) {
            throw ForbiddenException(ExceptionMessage.USER_ACCESS_RESTRICTION)
        }

        return joinPoint.proceed()
    }
}