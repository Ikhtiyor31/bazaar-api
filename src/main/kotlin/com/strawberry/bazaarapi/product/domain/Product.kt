package com.strawberry.bazaarapi.product.domain

import com.strawberry.bazaarapi.category.domain.Category
import com.strawberry.bazaarapi.category.dto.CategoryRequest
import com.strawberry.bazaarapi.common.entity.BaseEntity
import com.strawberry.bazaarapi.product.dto.ProductRequest
import com.strawberry.bazaarapi.user.domain.User
import com.strawberry.bazaarapi.user.domain.UserLocation
import org.hibernate.annotations.Where
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(
    name = "products", indexes = [
        Index(columnList = "product_id", name = "idx_product_id"),
        Index(columnList = "seller_id", name = "idx_product_seller_id")
    ]
)
@AttributeOverride(name = "id", column = Column(name = "product_id"))
@Where(clause = "deleted=0")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    val id: Int = 0,

    @Column(name = "seller_id", nullable = false)
    val sellerId: Int,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String?,

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    var price: BigDecimal,

    @Column(name = "condition", nullable = false)
    var condition: String,

    @ManyToOne(fetch = FetchType.LAZY)
    var userLocation: UserLocation? = null,

    @Column(name = "is_sold", nullable = false)
    var isSold: Boolean = false,

    @Column(name = "is_negotiable", nullable = false)
    var isNegotiable: Boolean = false,

    @Column(name = "is_hidden", nullable = false)
    var isHidden: Boolean = false,

    @Column(name = "favorite_count", nullable = false)
    var favoriteCount: Int = 0,

    @Column(name = "view_count", nullable = false)
    var viewCount: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", insertable = false, updatable = false)
    var seller: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    var category: Category? = null,

    @OneToMany(
        mappedBy = "product",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY,
        targetEntity = ProductImage::class
    )
    var images: List<ProductImage> = emptyList()
) : BaseEntity() {

    fun updateProduct(productRequest: ProductRequest): Product {
        title = productRequest.title
        description = productRequest.description
        price = productRequest.price
        condition = productRequest.condition
        userLocation = productRequest.userLocation
        isSold = productRequest.isSold
        isNegotiable = productRequest.isNegotiable
        isHidden = productRequest.isHidden
        category = category

        return this
    }

    fun hideProduct(): Product {
        return copy(isHidden = true)
    }

    fun markAsSold() {
        isSold = true
    }
}
