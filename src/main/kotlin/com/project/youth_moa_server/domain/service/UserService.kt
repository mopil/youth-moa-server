package com.project.youth_moa_server.domain.service

import com.project.youth_moa_server.api.dto.request.CreateUserRequest
import com.project.youth_moa_server.api.dto.request.UserLoginRequest
import com.project.youth_moa_server.api.dto.response.UserLoginResponse
import com.project.youth_moa_server.api.dto.response.UserResponse
import com.project.youth_moa_server.common.exception.UnauthorizedException
import com.project.youth_moa_server.domain.repository.UserRepository
import com.project.youth_moa_server.domain.repository.findByEmailOrThrow
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.math.exp

interface UserService {
    fun signUp(request: CreateUserRequest): UserLoginResponse
    fun login(request: UserLoginRequest): UserLoginResponse

    @Service
    @Transactional(readOnly = true)
    class Default(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
    ) : UserService {

        @Transactional
        override fun signUp(request: CreateUserRequest): UserLoginResponse {
            val encPassword = passwordEncoder.encode(request.password)
            val user = userRepository.save(request.toEntity(encPassword))

            // TODO: JWT 생성 로직

            return UserLoginResponse(
                userInfo = UserResponse.from(user),
                accessToken = "asdfasdf",
                expiredAt = LocalDateTime.now()
            )
        }

        override fun login(request: UserLoginRequest): UserLoginResponse {
            val user = userRepository.findByEmailOrThrow(request.email)

            if (passwordEncoder.encode(request.password) != user.encPassword) {
                throw UnauthorizedException()
            }

            // TODO: JWT 생성 로직

            return UserLoginResponse(
                userInfo = UserResponse.from(user),
                accessToken = "asdfasdf",
                expiredAt = LocalDateTime.now()
            )
        }

    }
}