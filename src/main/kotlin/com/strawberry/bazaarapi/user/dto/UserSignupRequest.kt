package com.strawberry.bazaarapi.user.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.strawberry.bazaarapi.common.validation.ValidationOrder
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size


data class UserSignupRequest(

        @JsonProperty
        var name: String = "",

        @JsonProperty
        @get:NotEmpty(message = "EMPTY_EMAIL")
        @get:Pattern(
                regexp = "^[0-9a-zA-Z_\\-.]*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$",
                message = "USER_EMAIL_CONDITION_NOT_MET",
                groups = [ValidationOrder.First::class]
        )
        var email: String = "",

        @JsonProperty
        @get:NotEmpty(message = "EMPTY_PASSWORD")
        @get:Size(min = 6, message = "PASSWORD_MIN_LENGTH_CONDITION_NOT_MET", groups = [ValidationOrder.Second::class])
        var password: String = "",
)