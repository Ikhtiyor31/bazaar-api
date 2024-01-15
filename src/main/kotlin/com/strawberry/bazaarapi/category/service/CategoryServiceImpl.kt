package com.strawberry.bazaarapi.category.service

import com.strawberry.bazaarapi.category.domain.Category
import com.strawberry.bazaarapi.category.dto.CategoryDTO
import com.strawberry.bazaarapi.category.dto.CategoryRequest
import com.strawberry.bazaarapi.category.dto.toCategoryDTO
import com.strawberry.bazaarapi.category.respository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
): CategoryService {

    override fun createCategory(category: Category): CategoryDTO {
        val savedCategory = categoryRepository.save(category)

        return savedCategory
    }

    @Transactional(readOnly = true)
    override fun getCategoryById(categoryId: Long): CategoryDTO {
        val category = categoryRepository.findById(categoryId)

        return category.toCategoryDTO()
    }

    @Transactional(readOnly = true)
    override fun getAllCategories(): List<CategoryDTO> {
        val categories = categoryRepository.getCategoryList()

        return categories.map { it.toCategoryDTO() }
    }

    override fun updateCategory(categoryId: Long, categoryRequest: CategoryRequest): CategoryDTO {
        val category = categoryRepository.findById(categoryId)
        val updatedCategory = category.update(categoryRequest)

        return updatedCategory.toCategoryDTO()
    }

    override fun deleteCategory(categoryId: Long) {
        val category = categoryRepository.findById(categoryId)
        category.delete()
    }
}