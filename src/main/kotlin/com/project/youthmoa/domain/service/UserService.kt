package com.project.youthmoa.domain.service

import com.project.youthmoa.api.dto.request.CreateUserRequest
import com.project.youthmoa.api.dto.request.UpdateUserInfoRequest
import com.project.youthmoa.api.dto.request.UserLoginRequest
import com.project.youthmoa.api.dto.response.UserLoginResponse
import com.project.youthmoa.api.dto.response.UserResponse
import com.project.youthmoa.common.auth.AuthenticationUtils.checkIsSelf
import com.project.youthmoa.common.exception.ErrorType
import com.project.youthmoa.common.exception.UnauthorizedException
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.findByEmailOrThrow
import com.project.youthmoa.domain.repository.findByIdOrThrow
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UserService {
    fun signUp(request: CreateUserRequest): UserLoginResponse

    fun login(request: UserLoginRequest): UserLoginResponse

    fun updateUserInfo(
        userId: Long,
        request: UpdateUserInfoRequest,
    ): UserResponse

    fun resetPassword(email: String)

    fun withdraw(userId: Long)

    @Service
    @Transactional(readOnly = true)
    class Default(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val tokenService: TokenService,
        private val emailService: EmailService,
    ) : UserService {
        @Transactional
        override fun signUp(request: CreateUserRequest): UserLoginResponse {
            val encPassword = passwordEncoder.encode(request.password)

            val user = userRepository.save(request.toEntity(encPassword))

            val tokenInfo = tokenService.generateAccessToken(user.id, user.role.name)

            return UserLoginResponse(
                userInfo = UserResponse.from(user),
                tokenInfo = tokenInfo,
            )
        }

        override fun login(request: UserLoginRequest): UserLoginResponse {
            val user = userRepository.findByEmailOrThrow(request.email)

            if (passwordEncoder.matches(request.password, user.encPassword).not()) {
                throw UnauthorizedException(ErrorType.INVALID_PASSWORD.defaultMessage)
            }

            val tokenInfo = tokenService.generateAccessToken(user.id, user.role.name)

            return UserLoginResponse(
                userInfo = UserResponse.from(user),
                tokenInfo = tokenInfo,
            )
        }

        @Transactional
        override fun updateUserInfo(
            userId: Long,
            request: UpdateUserInfoRequest,
        ): UserResponse {
            checkIsSelf(userId)

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

            emailService.sendTempPassword(user.email, randomPassword)
        }

        @Transactional
        override fun withdraw(userId: Long) {
            checkIsSelf(userId)

            val user = userRepository.findByIdOrThrow(userId)

            user.isDeleted = true
            user.applications.forEach { application ->
                application.apply {
                    isDeleted = true
                }
            }
        }
    }
}
