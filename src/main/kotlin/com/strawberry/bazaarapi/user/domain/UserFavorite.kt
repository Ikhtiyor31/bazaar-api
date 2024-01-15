package com.strawberry.bazaarapi.user.domain

import com.strawberry.bazaarapi.common.entity.BaseEntity
import com.strawberry.bazaarapi.product.domain.Product
import javax.persistence.Entity
import javax.persistence.*

@Entity
@Table(name = "user_favorites")
data class UserFavorite(
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
