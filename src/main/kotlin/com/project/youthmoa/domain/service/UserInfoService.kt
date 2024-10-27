package com.project.youthmoa.domain.service

import com.project.youthmoa.api.app.request.UpdateUserInfoRequest
import com.project.youthmoa.api.app.response.UserResponse
import com.project.youthmoa.common.util.SendEmail
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.findByEmailOrThrow
import com.project.youthmoa.domain.repository.findByIdOrThrow
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UserInfoService {
    fun updateUserInfo(
        userId: Long,
        request: UpdateUserInfoRequest,
    ): UserResponse

    fun resetPassword(email: String)

    fun withdraw(userId: Long)
}

@Service
@Transactional(readOnly = true)
class UserInfoServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val sendEmail: SendEmail,
) : UserInfoService {
    @Transactional
    override fun updateUserInfo(
        userId: Long,
        request: UpdateUserInfoRequest,
    ): UserResponse {
        val user = userRepository.findByIdOrThrow(userId)

        user.apply {
            name = request.newName
            encPassword = passwordEncoder.encode(request.newPassword)
            gender = request.newGender
            address = request.newAddress
            phone = request.newPhone
        }
        return UserResponse.from(user)
    }

    @Transactional
    override fun resetPassword(email: String) {
        val user = userRepository.findByEmailOrThrow(email)

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
