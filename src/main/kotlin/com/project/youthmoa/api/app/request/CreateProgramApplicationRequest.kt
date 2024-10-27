package com.project.youthmoa.api.app.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

data class CreateProgramApplicationRequest(
    val programId: Long,
    val applierName: String,
    @Schema(description = "휴대폰 번호(숫자만)", example = "01012341234")
    @field:Pattern(regexp = ".*[0-9].*", message = "휴대폰 번호는 숫자만 입력해야 합니다.")
    val applierPhone: String,
    @Schema(description = "ID(email)", example = "mopil1102@gmail.com")
    @field:Email
    val applierEmail: String,
    val applierAddress: String,
    val attachmentFileIds: List<Long> = emptyList(),
)
