package com.project.youthmoa.api.dto.response

data class UserLoginResponse(
    val userInfo: UserResponse,
    val tokenInfo: TokenResponse,
)
