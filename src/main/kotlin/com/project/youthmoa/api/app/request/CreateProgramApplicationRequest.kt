package com.project.youthmoa.api.app.request

import com.project.youthmoa.domain.vo.Email
import com.project.youthmoa.domain.vo.Phone
import io.swagger.v3.oas.annotations.media.Schema

data class CreateProgramApplicationRequest(
    val programId: Long,
    val applierName: String,
    @Schema(description = "휴대폰 번호(숫자만)", example = "01012341234")
    val applierPhone: String,
    @Schema(description = "ID(email)", example = "mopil1102@gmail.com")
    val applierEmail: String,
    val applierAddress: String,
    val attachmentFileIds: List<Long> = emptyList(),
) {
    init {
        Phone(applierPhone)
        Email(applierEmail)
    }
}
