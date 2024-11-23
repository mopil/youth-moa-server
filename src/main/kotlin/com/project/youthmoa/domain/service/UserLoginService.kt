package com.project.youthmoa.domain.service

import com.project.youthmoa.api.app.request.CreateUserRequest
import com.project.youthmoa.api.app.request.UserLoginRequest
import com.project.youthmoa.api.app.response.UserLoginResponse
import com.project.youthmoa.api.common.response.UserResponse
import com.project.youthmoa.common.exception.ErrorType
import com.project.youthmoa.common.exception.UnauthorizedException
import com.project.youthmoa.common.util.TokenManager
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.findByEmailOrThrow
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

interface UserLoginService {
    fun signUp(request: CreateUserRequest): UserLoginResponse

    fun login(request: UserLoginRequest): UserLoginResponse
}

@Service
@Transactional
class UserLoginServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenManager: TokenManager,
) : UserLoginService {
    override fun signUp(request: CreateUserRequest): UserLoginResponse {
        val encPassword = passwordEncoder.encode(request.password)

        val user = userRepository.save(request.toEntity(encPassword))

        val tokenInfo = tokenManager.generateToken(user.id, user.role.name)

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

        user.lastLoginedAt = LocalDateTime.now()

        val tokenInfo = tokenManager.generateToken(user.id, user.role.name)

        return UserLoginResponse(
            userInfo = UserResponse.from(user),
            tokenInfo = tokenInfo,
        )
    }
}
