package com.strawberry.bazaarapi.product.repository

import com.strawberry.bazaarapi.product.domain.Product
import com.strawberry.bazaarapi.product.dto.ProductDTO

interface ProductRepository {
    fun save(product: Product): ProductDTO
    fun findById(productId: Long): Product
}