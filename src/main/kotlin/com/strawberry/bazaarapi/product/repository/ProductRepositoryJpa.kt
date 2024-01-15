package com.strawberry.bazaarapi.product.repository

import com.strawberry.bazaarapi.product.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepositoryJpa: JpaRepository<Product, Long>