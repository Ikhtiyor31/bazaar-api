package com.strawberry.bazaarapi.user.dto

import com.strawberry.bazaarapi.common.validation.ValidationOrder
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


data class UserSignupRequest(
    var name: String = "",

    @get:NotBlank(message = "Email can not be empty", groups = [ValidationOrder.First::class])
    @get:Email(message = "Invalid email address", groups = [ValidationOrder.Second::class])
    val email: String,

    @get:NotBlank(message = "Password can not be empty", groups = [ValidationOrder.Third::class])
    @get:Size(min = 6, message = "Password must be at least 6 characters", groups = [ValidationOrder.Fourth::class])
    val password: String,
)