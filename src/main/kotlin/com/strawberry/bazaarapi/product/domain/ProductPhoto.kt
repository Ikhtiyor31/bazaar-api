package com.strawberry.bazaarapi.product.domain

import com.strawberry.bazaarapi.common.entity.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "product_photos")
data class ProductPhoto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_photo_id")
    val id: Int = 0,

    @Column(name = "product_id", nullable = false)
    val productId: Int,

    @Column(name = "photo_url", nullable = false, columnDefinition = "TEXT")
    val photoUrl: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    val product: Product? = null
) : BaseEntity()
