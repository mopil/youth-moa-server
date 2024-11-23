package com.project.youthmoa.api.app.response

import com.project.youthmoa.api.common.response.TokenResponse
import com.project.youthmoa.api.common.response.UserResponse

data class UserLoginResponse(
    val userInfo: UserResponse,
    val tokenInfo: TokenResponse,
)
