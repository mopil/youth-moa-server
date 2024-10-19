package com.project.youthmoa.domain.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

interface EmailService {
    fun sendTempPassword(
        receiverEmail: String,
        tempPassword: String,
    )
}

@Service
class EmailServiceImpl(
    private val mailSender: JavaMailSender,
) : EmailService {
    override fun sendTempPassword(
        receiverEmail: String,
        tempPassword: String,
    ) {
        try {
            val mimeMessage = mailSender.createMimeMessage()

            MimeMessageHelper(mimeMessage).apply {
                setTo(receiverEmail)
                setSubject("[청년모아] 임시 비밀번호 발급")
                setText("임시 비밀번호: $tempPassword \n로그인 후 비밀번호를 변경해주세요.")
            }.let { mailSender.send(it.mimeMessage) }
        } catch (e: Exception) {
            throw IllegalArgumentException("이메일 전송에 실패했습니다. ${e.message}")
        }
    }
}
