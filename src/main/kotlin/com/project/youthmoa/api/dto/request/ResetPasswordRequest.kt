package com.project.youthmoa.api.dto.request

import com.project.youthmoa.domain.vo.Email

data class ResetPasswordRequest(
    val email: Email,
)
