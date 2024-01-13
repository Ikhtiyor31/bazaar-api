package com.strawberry.bazaarapi.product.domain

import com.strawberry.bazaarapi.common.entity.BaseEntity
import com.strawberry.bazaarapi.user.domain.User
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    val id: Int = 0,

    @Column(name = "seller_id", nullable = false)
    val sellerId: Int,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String?,

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    val price: BigDecimal,

    @Column(name = "condition", nullable = false)
    val condition: String,

    @Column(name = "location", nullable = true)
    val location: String?,

    @Column(name = "is_sold", nullable = false)
    val isSold: Boolean = false,

    @Column(name = "is_negotiable", nullable = false)
    val isNegotiable: Boolean = false,

    @Column(name = "favorite_count", nullable = false)
    val favoriteCount: Int = 0,

    @Column(name = "view_count", nullable = false)
    val viewCount: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", insertable = false, updatable = false)
    val seller: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    val category: Category? = null,

    @OneToMany(
        mappedBy = "product",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    val photos: List<ProductPhoto> = mutableListOf()
) : BaseEntity()
