package com.strawberry.bazaarapi.product.service

import com.strawberry.bazaarapi.category.respository.CategoryRepository
import com.strawberry.bazaarapi.product.dto.*
import com.strawberry.bazaarapi.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
): ProductService {
    override fun createProduct(productRequest: ProductRequest): ProductDTO {
        val category = categoryRepository.findById(productRequest.categoryId)
        val product = productRequest.toProductEntity(category)

        return productRepository.save(product)
    }

    override fun updateProduct(productRequest: ProductRequest, productId: Long): ProductDTO {
        val category = categoryRepository.findById(productRequest.categoryId)
        val product = productRepository.findById(productId)
        val updatedProduct = product.updateProduct(productRequest)

        return updatedProduct.toProductDTO()
    }

    override fun deleteProduct(productId: Long) {
        val product = productRepository.findById(productId)
        product.delete()
    }

    override fun hideProduct(productId: Long) {
        val product = productRepository.findById(productId)
        product.hideProduct()
    }

    override fun getProductDetail(productId: Long): ProductDetailDTO {
        val product = productRepository.findById(productId)

        return product.toProductDetailDTO()
    }
} // gachon