package com.strawberry.bazaarapi.user.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class PasswordResetRequest(
    @JsonProperty
    val email: String,

    @JsonProperty
    val newPassword: String,

    @JsonProperty
    val confirmNewPassword: String,

)
