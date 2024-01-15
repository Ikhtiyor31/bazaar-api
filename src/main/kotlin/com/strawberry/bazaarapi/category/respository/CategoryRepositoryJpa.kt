package com.strawberry.bazaarapi.category.respository

import com.strawberry.bazaarapi.category.domain.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepositoryJpa: JpaRepository<Category, Long> {
}