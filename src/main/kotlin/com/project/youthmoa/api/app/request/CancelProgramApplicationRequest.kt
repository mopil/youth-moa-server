package com.project.youthmoa.api.app.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class CancelProgramApplicationRequest(
    @Schema(description = "취소 사유", example = "변심으로 인한 취소")
    @field:Pattern(regexp = ".{1,100}", message = "취소사유는 100자 이내로 입력해주세요")
    @field:NotBlank(message = "취소사유를 입력해주세요")
    val cancelReason: String,
)
