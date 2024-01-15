package com.strawberry.bazaarapi.category.service

import com.strawberry.bazaarapi.category.domain.Category
import com.strawberry.bazaarapi.category.dto.CategoryDTO
import com.strawberry.bazaarapi.category.dto.CategoryRequest

interface CategoryService {
    fun createCategory(category: Category): CategoryDTO
    fun getCategoryById(categoryId: Long): CategoryDTO
    fun getAllCategories(): List<CategoryDTO>
    fun updateCategory(categoryId: Long, categoryRequest: CategoryRequest): CategoryDTO
    fun deleteCategory(categoryId: Long)
}