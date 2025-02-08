package com.project.youthmoa.domain.service

import com.project.youthmoa.api.controller.user.request.CreateUserRequest
import com.project.youthmoa.api.controller.user.request.UserLoginRequest
import com.project.youthmoa.api.controller.user.response.UserInfoResponse
import com.project.youthmoa.api.controller.user.response.UserLoginResponse
import com.project.youthmoa.common.exception.ErrorType
import com.project.youthmoa.common.exception.UnauthorizedException
import com.project.youthmoa.common.util.TokenManager
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.findUniqueByEmailOrThrow
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
            userInfo = UserInfoResponse.from(user),
            tokenInfo = tokenInfo,
        )
    }

    override fun login(request: UserLoginRequest): UserLoginResponse {
        val user = userRepository.findUniqueByEmailOrThrow(request.email)

        if (passwordEncoder.matches(request.password, user.encPassword).not()) {
            throw UnauthorizedException(ErrorType.INVALID_PASSWORD.defaultMessage)
        }

        user.lastLoginedAt = LocalDateTime.now()

        val tokenInfo = tokenManager.generateToken(user.id, user.role.name)

        return UserLoginResponse(
            userInfo = UserInfoResponse.from(user),
            tokenInfo = tokenInfo,
        )
    }
}
