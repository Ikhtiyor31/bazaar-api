package com.strawberry.bazaarapi.util

import com.strawberry.bazaarapi.user.enums.Role
import javax.persistence.AttributeConverter

class UserRoleConverter : AttributeConverter<Role, String> {
    override fun convertToDatabaseColumn(attribute: Role?): String {
        return when (attribute) {
            Role.MANAGER -> "MANAGER"
            Role.ADMIN -> "ADMIN"
            else -> "USER"
        }
    }

    override fun convertToEntityAttribute(dbData: String?): Role {
        return when (dbData) {
            "MANAGER" -> Role.MANAGER
            "ADMIN" -> Role.ADMIN
            else -> Role.USER
        }
    }
}