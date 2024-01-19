package com.strawberry.bazaarapi.product.domain

import com.strawberry.bazaarapi.common.entity.BaseEntity
import org.hibernate.annotations.Where
import javax.persistence.*

@Entity
@Table(name = "product_images", indexes = [
    Index(columnList = "product_image_id", name = "idx_product_image_id"),
])
@Where(clause = "deleted=false")
data class ProductImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    val id: Int = 0,

    @Column(name = "image_url", nullable = false, length = 255, columnDefinition = "TEXT")
    val imageUrl: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product? = null
) : BaseEntity()
