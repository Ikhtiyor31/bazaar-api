package com.strawberry.bazaarapi.product.dto

import com.strawberry.bazaarapi.product.domain.ProductImage

data class ProductImageDTO(
    val imageUrl: String
)

fun ProductImage.toProductImageDTO(): ProductImageDTO {
    return ProductImageDTO(imageUrl = this.imageUrl)
}
