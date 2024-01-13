package com.strawberry.bazaarapi.user.domain

import com.strawberry.bazaarapi.common.entity.BaseEntity
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "email_verification_codes")
data class EmailVerificationCode(

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(name = "authorization_number")
    var confirmationCode: Long = 0L,

    @Column(name = "attempt_count")
    var attemptCount: Int = 0,

    @Column(name = "expiresAt")
    var expiresAt: LocalDateTime? = null,

    @Column(name = "confirmedAt")
    var confirmedAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User = User()

): BaseEntity() {
    fun updateConfirmedAt(localDateTime: LocalDateTime) {
        this.confirmedAt = localDateTime
    }
}
