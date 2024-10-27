package com.project.youthmoa.api.app.request

import jakarta.validation.constraints.Email

data class UserLoginRequest(
    @field:Email
    val email: String,
    val password: String,
)
