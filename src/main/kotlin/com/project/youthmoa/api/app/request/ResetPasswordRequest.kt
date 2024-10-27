package com.project.youthmoa.api.app.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email

data class ResetPasswordRequest(
    @Schema(description = "ID(email)", example = "mopil1102@gmail.com")
    @field:Email
    val email: String,
)
