package com.strawberry.bazaarapi.common.security

import com.strawberry.bazaarapi.user.enums.Role

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RoleMapping(vararg val value: Role = [])