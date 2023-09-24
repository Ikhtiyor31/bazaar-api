package com.strawberry.bazaarapi.user.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class RefreshTokenRequest(
        @JsonProperty
        var refreshToken: String,

        @JsonProperty
        var username: String
){
}