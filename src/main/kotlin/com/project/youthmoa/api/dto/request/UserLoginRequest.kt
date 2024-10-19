package com.project.youthmoa.api.dto.request

import com.project.youthmoa.domain.vo.Email

data class UserLoginRequest(
    val email: String,
    val password: String,
) {
    init {
        Email(email)
    }
}
