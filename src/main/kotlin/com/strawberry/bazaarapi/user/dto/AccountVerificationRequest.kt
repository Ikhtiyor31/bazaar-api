package com.strawberry.bazaarapi.user.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AccountVerificationRequest(

        @JsonProperty
        var email: String,

        @JsonProperty
        var confirmationCode: Long
)