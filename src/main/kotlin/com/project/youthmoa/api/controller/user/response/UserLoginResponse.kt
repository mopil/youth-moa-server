package com.project.youthmoa.api.controller.user.response

import com.project.youthmoa.api.controller.common.response.TokenResponse

data class UserLoginResponse(
    val userInfo: UserInfoResponse,
    val tokenInfo: TokenResponse,
)
