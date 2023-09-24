package com.strawberry.bazaarapi.email

data class EmailDetails(
        val recipient: String,
        val msgBody: String,
        val subject: String,
        val attachment: String = ""
)
