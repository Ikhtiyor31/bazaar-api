package com.strawberry.bazaarapi.category.dto

import com.strawberry.bazaarapi.category.domain.Category

data class CategoryDTO(
    val categoryId: Long,
    var name: String
)

fun Category.toCategoryDTO(): CategoryDTO {
    return CategoryDTO(
        categoryId = this.id,
        name = this.name
    )
}

fun CategoryRequest.toCategoryEntity(): Category {
    return Category(
        name = this.name
    )
}

fun CategoryDTO.toCategoryEntity(): Category {
    return Category(
        name = this.name
    )
}


