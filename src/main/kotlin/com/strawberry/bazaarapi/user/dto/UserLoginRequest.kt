package com.strawberry.bazaarapi.user.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UserLoginRequest(

        @JsonProperty
        var email: String,

        @JsonProperty
        var password: String,
)
