package com.strawberry.bazaarapi.email.repository


import com.strawberry.bazaarapi.user.domain.EmailVerificationCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailVerificationRepository : JpaRepository<EmailVerificationCode, Long> {
    fun findTopByUserIdOrderByIdDesc(userId: Long?): EmailVerificationCode?
}