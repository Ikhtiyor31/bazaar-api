package com.strawberry.bazaarapi.user.repository

import com.strawberry.bazaarapi.user.domain.UserToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJwtAccessTokenRepository : JpaRepository<UserToken, Long> {

    fun findTopByUsernameOrderByIdDesc(username: String) : UserToken?
}