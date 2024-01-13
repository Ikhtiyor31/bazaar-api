package com.strawberry.bazaarapi.product.domain

import com.strawberry.bazaarapi.common.entity.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "categories")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "name", nullable = false)
    val name: String

) : BaseEntity()
