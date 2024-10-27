package com.project.youthmoa.api.app.request

import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.constraints.Pattern

data class FindEmailRequest(
    @field:Parameter(description = "유저 이름", required = true)
    val name: String,
    @field:Parameter(description = "휴대폰 번호 (오직 숫자만)", required = true)
    @field:Pattern(regexp = ".*[0-9].*", message = "휴대폰 번호는 숫자만 입력해야 합니다.")
    val phone: String,
)
