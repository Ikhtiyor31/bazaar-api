package com.strawberry.bazaarapi.email.service

import com.strawberry.bazaarapi.email.EmailDetails

interface EmailService {
    fun sendSimpleMail(details: EmailDetails)
    fun sendMailWithAttachment(details: EmailDetails): String?

}