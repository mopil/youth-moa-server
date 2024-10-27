package com.project.youthmoa.api.app.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class CancelProgramApplicationRequest(
    @field:Pattern(regexp = ".{1,100}", message = "취소사유는 100자 이내로 입력해주세요")
    @field:NotBlank(message = "취소사유를 입력해주세요")
    val cancelReason: String,
)
