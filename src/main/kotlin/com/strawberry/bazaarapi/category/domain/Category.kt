package com.strawberry.bazaarapi.category.domain

import com.strawberry.bazaarapi.category.dto.CategoryRequest
import com.strawberry.bazaarapi.common.entity.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "categories", indexes = [
    Index(columnList = "name", name = "idx_category_name_idx")
])
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "name", nullable = false)
    var name: String

) : BaseEntity() {

    fun update(categoryRequest: CategoryRequest): Category {
        name = categoryRequest.name

        return this
    }
}
