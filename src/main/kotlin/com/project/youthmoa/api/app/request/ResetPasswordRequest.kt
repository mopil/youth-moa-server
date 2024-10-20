package com.project.youthmoa.api.app.request

import com.project.youthmoa.domain.vo.Email
import io.swagger.v3.oas.annotations.media.Schema

data class ResetPasswordRequest(
    @Schema(description = "ID(email)", example = "mopil1102@gmail.com")
    val email: String,
) {
    init {
        Email(email)
    }
}
