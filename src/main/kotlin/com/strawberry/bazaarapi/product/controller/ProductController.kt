package com.strawberry.bazaarapi.product.controller

import com.strawberry.bazaarapi.product.dto.ProductRequest
import com.strawberry.bazaarapi.product.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/products")
class ProductController(
    private val productService: ProductService
) {
    @PostMapping
    fun createProduct(@RequestBody saveProductRequest: ProductRequest): ResponseEntity<Any> {
        val savedProduct = productService.createProduct(saveProductRequest)

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct)
    }

    @PutMapping("/{productId}")
    fun updateProduct(@RequestBody updateProductRequest: ProductRequest, @PathVariable productId: Long): ResponseEntity<Any> {
        val updatedProduct = productService.updateProduct(updateProductRequest, productId)

        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct)
    }

    @DeleteMapping("/{productId}")
    fun deleteProduct(@PathVariable productId: Long): ResponseEntity<Any> {
        productService.deleteProduct(productId)

        return ResponseEntity.status(HttpStatus.OK).body("OK")
    }

    @GetMapping("/{productId}")
    fun getProductDetail(@PathVariable productId: Long): ResponseEntity<Any> {
        val productDetail = productService.getProductDetail(productId)

        return ResponseEntity.status(HttpStatus.OK).body(productDetail)
    }

    @PatchMapping("/{productId}/hide")
    fun hideProduct(@PathVariable productId: Long): ResponseEntity<Any> {
        val hiddenProduct = productService.hideProduct(productId)
        return ResponseEntity.ok(hiddenProduct)
    }


}