package com.strawberry.bazaarapi.product.dto

import com.strawberry.bazaarapi.product.domain.Product
import com.strawberry.bazaarapi.user.domain.UserLocation
import java.math.BigDecimal

data class ProductDetailDTO(
    val sellerId: Int,
    val title: String,
    val description: String?,
    val price: BigDecimal,
    val condition: String,
    val userLocation: UserLocation?,
    val isSold: Boolean = false,
    val isNegotiable: Boolean = false,
    val isHidden: Boolean = false,
    val favoriteCount: Int = 0,
    val viewCount: Int = 0,
    val categoryId: Long?,
    val photos: List<ProductImageDTO> = emptyList()
)

fun Product.toProductDetailDTO() : ProductDetailDTO {
    return ProductDetailDTO(
        sellerId = this.sellerId,
        title = this.title,
        description = this.description,
        price = this.price,
        condition = this.condition,
        userLocation = this.userLocation,
        isSold = this.isSold,
        isNegotiable = this.isNegotiable,
        isHidden = this.isHidden,
        favoriteCount = this.favoriteCount,
        viewCount = this.viewCount,
        categoryId = this.category?.id,
        photos = this.images.map { it.toProductImageDTO() }
    )
}
