package com.strawberry.bazaarapi.user.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.strawberry.bazaarapi.user.domain.QUser.user
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class UserRepositorySupportImpl(
    @Autowired val queryFactory: JPAQueryFactory
): UserRepositorySupport {

    override fun updateUserPassword(password: String, userId: Long) {
        queryFactory
            .update(user)
            .set(user.passwordHashed, password)
            .where(user.id.eq(userId))
            .execute()
    }
}