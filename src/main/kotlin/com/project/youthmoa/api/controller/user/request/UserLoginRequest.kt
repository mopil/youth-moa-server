package com.project.youthmoa.api.controller.user.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email

data class UserLoginRequest(
    @Schema(description = "ID(email)", example = "mopil1102@gmail.com")
    @field:Email
    val email: String,
    @Schema(description = "비밀번호", example = "1234")
    val password: String,
)
