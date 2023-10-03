package com.strawberry.bazaarapi.common.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.strawberry.bazaarapi.util.TimeUtil.getCurrentLocalTimeInUZT
import org.hibernate.annotations.DynamicUpdate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
@DynamicUpdate
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties("hibernateLazyInitializer")
abstract class BaseEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    private var createdAt: LocalDateTime = getCurrentLocalTimeInUZT()

    @Column(name = "update_at", nullable = false)
    private var updatedAt: LocalDateTime = getCurrentLocalTimeInUZT()

    @Column(name = "deleted", nullable = false, columnDefinition = "BIT default 0")
    private var deleted: Boolean = false

    fun delete() {
        this.deleted = true
    }
}
