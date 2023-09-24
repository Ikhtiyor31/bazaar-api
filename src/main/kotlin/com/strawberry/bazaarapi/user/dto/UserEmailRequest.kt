package com.strawberry.bazaarapi.user.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UserEmailRequest(
    @JsonProperty
    val email: String
)
