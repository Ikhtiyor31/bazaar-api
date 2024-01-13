package com.strawberry.bazaarapi.user.dto

data class UserProfile (
    val name: String?,
    val email: String,
    val phoneNumber: String?,
    val profileUrl: String?,
    val address: String?
)
