package com.strawberry.bazaarapi.email.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.strawberry.bazaarapi.user.domain.EmailVerificationCode
import com.strawberry.bazaarapi.user.domain.QEmailVerificationCode.emailVerificationCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class EmailConfirmationRepositoryImpl(
    @Autowired val query: JPAQueryFactory
) {

    fun findConfirmationCodeByUserId(userId: Long, code: Long) : EmailVerificationCode? {
        return query
            .selectFrom(emailVerificationCode)
            .where(
                emailVerificationCode.user.id.eq(userId).and(
                emailVerificationCode.confirmationCode.eq(code)
            )).fetchFirst()

    }
}