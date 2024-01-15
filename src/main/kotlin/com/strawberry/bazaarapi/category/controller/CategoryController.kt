package com.strawberry.bazaarapi.category.controller

import com.strawberry.bazaarapi.category.dto.CategoryDTO
import com.strawberry.bazaarapi.category.dto.CategoryRequest
import com.strawberry.bazaarapi.category.dto.toCategoryEntity
import com.strawberry.bazaarapi.category.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    @PostMapping
    fun createCategory(@RequestBody categoryRequest: CategoryRequest): ResponseEntity<Any> {
        val createdCategory = categoryService.createCategory(categoryRequest.toCategoryEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory)
    }

    @PutMapping("/{categoryId}")
    fun updateCategory(
        @PathVariable categoryId: Long,
        @RequestBody categoryRequest: CategoryRequest
    ): ResponseEntity<CategoryDTO> {
        val updatedCategory = categoryService.updateCategory(categoryId, categoryRequest)
        return ResponseEntity.ok(updatedCategory)
    }

    @GetMapping("/{categoryId}")
    fun getCategory(@PathVariable categoryId: Long): ResponseEntity<CategoryDTO> {
        val category = categoryService.getCategoryById(categoryId)
        return ResponseEntity.ok(category)
    }

    @GetMapping
    fun getAllCategories(): ResponseEntity<List<CategoryDTO>> {
        val categories = categoryService.getAllCategories()
        return ResponseEntity.ok(categories)
    }

    @DeleteMapping("/{categoryId}")
    fun deleteCategory(@PathVariable categoryId: Long): ResponseEntity<Void> {
        categoryService.deleteCategory(categoryId)
        return ResponseEntity.noContent().build()
    }
}