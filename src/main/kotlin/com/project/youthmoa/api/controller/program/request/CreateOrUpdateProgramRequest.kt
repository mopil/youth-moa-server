package com.project.youthmoa.api.controller.program.request

import java.time.LocalDateTime

data class CreateOrUpdateProgramRequest(
    val programImageFileId: Long?,
    val title: String,
    val description: String?,
    val applyStartAt: LocalDateTime,
    val applyEndAt: LocalDateTime,
    val programStartAt: LocalDateTime,
    val programEndAt: LocalDateTime,
    val location: String?,
    val maxUserCount: Int,
    val contact: String?,
    val attachmentFileIds: List<Long> = emptyList(),
    val detailContent: String?,
    val youthCenterIds: List<Long>,
    val lectures: List<String>,
    val isAttachmentRequired: Boolean,
    val freeQuestions: List<String> = emptyList(),
) {
    init {
        require(attachmentFileIds.size <= 5) {
            "첨부파일은 최대 5개까지 가능합니다."
        }
    }
}
