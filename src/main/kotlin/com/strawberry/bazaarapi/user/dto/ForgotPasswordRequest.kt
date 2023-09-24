package com.strawberry.bazaarapi.user.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ForgotPasswordRequest(
    @JsonProperty
    val email: String,

    @JsonProperty
    val confirmationCode: String
)
