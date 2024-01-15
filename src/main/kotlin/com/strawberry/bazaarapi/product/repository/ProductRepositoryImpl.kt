package com.strawberry.bazaarapi.product.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.strawberry.bazaarapi.common.exception.ExceptionMessage
import com.strawberry.bazaarapi.product.domain.Product
import com.strawberry.bazaarapi.product.dto.ProductDTO
import com.strawberry.bazaarapi.product.dto.toProductDTO
import com.strawberry.bazaarapi.product.exception.ProductNotFoundException
import org.springframework.stereotype.Repository


@Repository
class ProductRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val productRepositoryJpa: ProductRepositoryJpa
): ProductRepository {

    override fun save(product: Product): ProductDTO {
        val savedProduct = productRepositoryJpa.save(product)

        return savedProduct.toProductDTO()
    }

    override fun findById(productId: Long): Product {
        return productRepositoryJpa.findById(productId)
            .orElseThrow{throw ProductNotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND, "productId=$productId")}
    }
}