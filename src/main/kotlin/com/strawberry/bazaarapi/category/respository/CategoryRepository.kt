package com.strawberry.bazaarapi.category.respository

import com.strawberry.bazaarapi.category.domain.Category
import com.strawberry.bazaarapi.category.dto.CategoryDTO

interface CategoryRepository {
    fun save(category: Category): CategoryDTO
    fun findById(categoryId: Long): Category
    fun getCategoryList(): List<Category>
}