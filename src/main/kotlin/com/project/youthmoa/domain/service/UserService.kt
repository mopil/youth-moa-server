package com.project.youthmoa.domain.service

import com.project.youthmoa.api.dto.request.CreateUserRequest
import com.project.youthmoa.api.dto.request.UserLoginRequest
import com.project.youthmoa.api.dto.response.UserLoginResponse
import com.project.youthmoa.api.dto.response.UserResponse
import com.project.youthmoa.common.exception.ErrorType
import com.project.youthmoa.common.exception.UnauthorizedException
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.findByEmailOrThrow
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface UserService {
    fun signUp(request: CreateUserRequest): UserLoginResponse

    fun login(request: UserLoginRequest): UserLoginResponse

    @Service
    @Transactional(readOnly = true)
    class Default(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val tokenService: TokenService,
    ) : UserService {
        @Transactional
        override fun signUp(request: CreateUserRequest): UserLoginResponse {
            val encPassword = passwordEncoder.encode(request.password)

            val user = userRepository.save(request.toEntity(encPassword))

            val tokenInfo = tokenService.generateAccessToken(user.id, "USER")

            return UserLoginResponse(
                userInfo = UserResponse.from(user),
                tokenInfo = tokenInfo,
            )
        }

        override fun login(request: UserLoginRequest): UserLoginResponse {
            val user = userRepository.findByEmailOrThrow(request.email)

            if (passwordEncoder.matches(request.password, user.encPassword)) {
                throw UnauthorizedException(ErrorType.INVALID_PASSWORD.defaultMessage)
            }

            val tokenInfo = tokenService.generateAccessToken(user.id, "USER")

            return UserLoginResponse(
                userInfo = UserResponse.from(user),
                tokenInfo = tokenInfo,
            )
        }
    }
}
