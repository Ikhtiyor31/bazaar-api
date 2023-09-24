package com.strawberry.bazaarapi.email.service

import com.strawberry.bazaarapi.common.exception.ApiRequestException
import com.strawberry.bazaarapi.common.exception.ExceptionMessage
import com.strawberry.bazaarapi.email.EmailDetails
import org.hibernate.bytecode.BytecodeLogging.LOGGER
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service


@Service
class EmailServiceImpl(
    @Autowired private val javaMailSender: JavaMailSender
) : EmailService {

    @Value("\${spring.mail.username}")
    private lateinit var sender: String

    @Async("taskExecutor")
    override fun sendSimpleMail(details: EmailDetails) {
        return try {
            val mimeMessage = javaMailSender.createMimeMessage()
            val mimeMessageHelper = MimeMessageHelper(mimeMessage, "utf-8")
            mimeMessageHelper.setText(buildEmail(details.msgBody), true)
            mimeMessageHelper.setTo(details.recipient)
            mimeMessageHelper.setSubject(details.subject)
            mimeMessageHelper.setFrom(sender)
            javaMailSender.send(mimeMessage)
        } catch (e: Exception) {
            LOGGER.error("failed to send email")
            throw ApiRequestException(ExceptionMessage.EMAIL_SENDING_ERROR, HttpStatus.BAD_GATEWAY);
        }
    }

    override fun sendMailWithAttachment(details: EmailDetails): String? {
        TODO("Not yet implemented")
    }

    companion object {
        private fun buildEmail(confirmationCode: String) : String {
           return  "<div> Hi, there</div>\n" +
            "<div> <p> Thank you for registering at Bazaar inc.</p>\n" +
            "<b style=\"font-size: 19px;\"> " + confirmationCode + "</b> </div> \n" +
           "<div> <p>the verification code will expire in 15 minites, </p> Best regards</div>\n"
        }
    }

}