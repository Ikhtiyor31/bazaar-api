package com.strawberry.bazaarapi.category.respository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.strawberry.bazaarapi.category.domain.Category
import com.strawberry.bazaarapi.category.dto.CategoryDTO
import com.strawberry.bazaarapi.category.dto.toCategoryDTO
import com.strawberry.bazaarapi.common.exception.CustomNotFoundException
import com.strawberry.bazaarapi.common.exception.ExceptionMessage
import org.springframework.stereotype.Repository

@Repository
class CategoryRepositoryIml(
    private val queryFactory: JPAQueryFactory,
    private val categoryRepositoryJpa: CategoryRepositoryJpa
): CategoryRepository {
    override fun save(category: Category): CategoryDTO {
        val savedCategory = categoryRepositoryJpa.save(category)

        return savedCategory.toCategoryDTO()
    }

    override fun findById(categoryId: Long): Category {
        return categoryRepositoryJpa.findById(categoryId)
            .orElseThrow{throw CustomNotFoundException(ExceptionMessage.CATEGORY_NOT_FOUND, "categoryId=${categoryId}") }
    }

    override fun getCategoryList(): List<Category> {
        return categoryRepositoryJpa.findAll()
    }
}