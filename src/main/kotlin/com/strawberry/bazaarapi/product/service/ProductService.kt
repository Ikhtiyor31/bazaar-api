package com.strawberry.bazaarapi.product.service

import com.strawberry.bazaarapi.product.dto.ProductDTO
import com.strawberry.bazaarapi.product.dto.ProductDetailDTO
import com.strawberry.bazaarapi.product.dto.ProductRequest

interface ProductService {
    fun createProduct(productRequest: ProductRequest): ProductDTO
    fun updateProduct(productRequest: ProductRequest, productId: Long): ProductDTO
    fun deleteProduct(productId: Long)
    fun hideProduct(productId: Long)
    fun getProductDetail(productId: Long): ProductDetailDTO
}