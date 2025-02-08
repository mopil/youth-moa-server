package com.project.youthmoa.domain.service

import com.project.youthmoa.api.controller.user.request.UpdateUserInfoRequest
import com.project.youthmoa.api.controller.user.response.UserInfoResponse
import com.project.youthmoa.common.util.SendEmail
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.repository.findUniqueByEmailOrThrow
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UserWriteService {
    fun updateUserInfo(
        userId: Long,
        request: UpdateUserInfoRequest,
    ): UserInfoResponse

    fun resetPassword(email: String)

    fun withdraw(userId: Long)
}

@Service
@Transactional(readOnly = true)
class UserWriteServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val sendEmail: SendEmail,
) : UserWriteService {
    @Transactional
    override fun updateUserInfo(
        userId: Long,
        request: UpdateUserInfoRequest,
    ): UserInfoResponse {
        val user = userRepository.findByIdOrThrow(userId)

        user.apply {
            name = request.newName
            encPassword = passwordEncoder.encode(request.newPassword)
            gender = request.newGender
            address = request.newAddress
            phone = request.newPhone
        }
        return UserInfoResponse.from(user)
    }

    @Transactional
    override fun resetPassword(email: String) {
        val user = userRepository.findUniqueByEmailOrThrow(email)

        val randomPassword = (100000..999999).random().toString()
        user.encPassword = passwordEncoder.encode(randomPassword)

        sendEmail(
            receiverEmail = user.email,
            title = "[청년모아] 임시 비밀번호 발급 안내",
            content = "임시 비밀번호는 $randomPassword 입니다. 로그인 후 비밀번호를 변경해주세요.",
        )
    }

    @Transactional
    override fun withdraw(userId: Long) {
        val user = userRepository.findByIdOrThrow(userId)

        user.isDeleted = true
        user.applications.forEach { application ->
            application.apply {
                isDeleted = true
            }
        }
    }
}
