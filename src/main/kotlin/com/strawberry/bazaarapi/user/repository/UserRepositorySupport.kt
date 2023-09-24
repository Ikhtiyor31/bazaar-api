package com.strawberry.bazaarapi.user.repository

interface UserRepositorySupport {
    fun updateUserPassword(password: String, userId: Long)
}