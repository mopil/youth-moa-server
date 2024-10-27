package com.project.youthmoa.api.admin.request

import java.time.LocalDate

data class CreateProgramRequest(
    val programImageFileId: Long?,
    val title: String,
    val description: String?,
    val applyStartDate: LocalDate,
    val applyEndDate: LocalDate,
    val programStartDate: LocalDate,
    val programEndDate: LocalDate,
    val location: String?,
    val maxUserCount: Int,
    val contactNumber: String?,
    val attachmentFileIds: List<Long> = emptyList(),
    val detailContent: String?,
    val youthCenterId: Long,
    val isNeedApprove: Boolean,
    val lectures: List<String>,
    val isAttachmentRequired: Boolean,
    // TODO : 질문이랑 약관 제공여부는 제외?
) {
    init {
        require(attachmentFileIds.size <= 5) {
            "첨부파일은 최대 5개까지 가능합니다."
        }
    }
}
