package com.strawberry.bazaarapi.user.enums

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.STRING)
enum class PlatformType {
    WEB,
    ANDROID,
    IOS,
    UNKNOWN
}