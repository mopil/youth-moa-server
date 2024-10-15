package com.project.youth_moa_server.api.dto.request

data class UserLoginRequest(
    val email: String,
    val password: String,
)