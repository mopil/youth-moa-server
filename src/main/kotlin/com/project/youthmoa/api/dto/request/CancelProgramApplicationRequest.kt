package com.project.youthmoa.api.dto.request

data class CancelProgramApplicationRequest(
    val cancelReason: String,
) {
    init {
        require(cancelReason.isNotBlank()) { "취소사유를 입력해주세요" }
        require(cancelReason.length <= 100) { "취소사유는 100자 이내로 입력해주세요" }
    }
}
