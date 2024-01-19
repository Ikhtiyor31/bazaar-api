package com.strawberry.bazaarapi.product.dto

import com.strawberry.bazaarapi.product.domain.Product
import com.strawberry.bazaarapi.category.domain.Category
import com.strawberry.bazaarapi.product.domain.ProductImage
import com.strawberry.bazaarapi.user.domain.UserLocation
import java.math.BigDecimal

data class ProductDTO(
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

fun Product.toProductDTO() : ProductDTO {
    return ProductDTO(
        title = this.title,
        description = this.description,
        price = this.price,
        condition = this.itemCondition,
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

fun ProductRequest.toProductEntity(category: Category): Product {
    return Product(
        title = this.title,
        description = this.description,
        price = this.price,
        itemCondition = this.condition,
        userLocation = this.userLocation,
        isSold = this.isSold,
        isNegotiable = this.isNegotiable,
        isHidden = this.isHidden,
        favoriteCount = this.favoriteCount,
        viewCount = this.viewCount,
        category = category,
        images = this.images.map { ProductImage(imageUrl = it.imageUrl) }
    )
}