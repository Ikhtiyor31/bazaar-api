package com.strawberry.bazaarapi.util

import com.strawberry.bazaarapi.user.enums.Roles
import javax.persistence.AttributeConverter

class UserRoleConverter : AttributeConverter<Roles, String> {
    override fun convertToDatabaseColumn(attribute: Roles?): String {
        return when (attribute) {
            Roles.MANAGER -> "MANAGER"
            Roles.ADMIN -> "ADMIN"
            else -> "USER"
        }
    }

    override fun convertToEntityAttribute(dbData: String?): Roles {
        return when (dbData) {
            "MANAGER" -> Roles.MANAGER
            "ADMIN" -> Roles.ADMIN
            else -> Roles.USER
        }
    }
}