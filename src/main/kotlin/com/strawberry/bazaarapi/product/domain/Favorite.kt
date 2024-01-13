package com.strawberry.bazaarapi.product.domain

import com.strawberry.bazaarapi.common.entity.BaseEntity
import com.strawberry.bazaarapi.user.domain.User
import javax.persistence.Entity
import javax.persistence.*

@Entity
@Table(name = "favorites")
data class Favorite(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,
) : BaseEntity()
