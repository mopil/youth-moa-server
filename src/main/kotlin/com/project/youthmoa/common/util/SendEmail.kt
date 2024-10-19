package com.project.youthmoa.common.util

import com.project.youthmoa.common.exception.InternalServerException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

fun interface SendEmail {
    operator fun invoke(
        receiverEmail: String,
        title: String,
        content: String,
    )

    @Component
    class Default(
        private val mailSender: JavaMailSender,
    ) : SendEmail {
        override fun invoke(
            receiverEmail: String,
            title: String,
            content: String,
        ) {
            try {
                val mimeMessage = mailSender.createMimeMessage()

                MimeMessageHelper(mimeMessage).apply {
                    setTo(receiverEmail)
                    setSubject(title)
                    setText(content)
                }.let { mailSender.send(it.mimeMessage) }
            } catch (e: Exception) {
                throw InternalServerException.ofSendEmailFailed()
            }
        }
    }
}
