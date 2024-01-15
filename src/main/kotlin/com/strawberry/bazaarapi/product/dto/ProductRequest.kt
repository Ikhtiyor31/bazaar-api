package com.strawberry.bazaarapi.product.dto

import com.strawberry.bazaarapi.user.domain.UserLocation
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive
import java.math.BigDecimal

data class ProductRequest(
    @field:NotNull(message = "sellerId is required field")
    @field:Positive(message = "sellerId must be a positive integer")
    val sellerId: Int,

    @field:NotEmpty(message = "title is required field")
    val title: String,

    @field:NotEmpty(message = "description is required field")
    val description: String?,

    @field:NotNull(message = "price is required field")
    @field:Positive(message = "price must be a positive number")
    val price: BigDecimal,

    @field:NotEmpty(message = "condition is required field")
    val condition: String,

    val userLocation: UserLocation?,

    val isSold: Boolean = false,
    val isNegotiable: Boolean = false,
    val isHidden: Boolean = false,

    @field:Positive(message = "favoriteCount must be a positive integer")
    val favoriteCount: Int = 0,

    @field:Positive(message = "viewCount must be a positive integer")
    val viewCount: Int = 0,

    @field:NotNull(message = "categoryId is required field")
    @field:Positive(message = "categoryId must be a positive integer")
    val categoryId: Long,

    val images: List<ProductImageRequest> = emptyList()
)