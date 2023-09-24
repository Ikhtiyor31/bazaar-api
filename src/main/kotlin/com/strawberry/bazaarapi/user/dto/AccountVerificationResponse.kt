package com.strawberry.bazaarapi.user.dto

data class AccountVerificationResponse(
        var isConfirmed: Boolean,
        var message: String
)