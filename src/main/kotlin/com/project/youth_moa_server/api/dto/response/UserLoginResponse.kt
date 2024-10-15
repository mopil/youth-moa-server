package com.project.youth_moa_server.api.dto.response

import java.time.LocalDateTime

data class UserLoginResponse(
    val userInfo: UserResponse,
    val accessToken: String,
    val expiredAt: LocalDateTime
)