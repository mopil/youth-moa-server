package com.project.youthmoa.api.controller.program.response

import com.project.youthmoa.domain.model.Program
import com.project.youthmoa.domain.type.ProgramStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ProgramInfoResponse(
    val programId: Long,
    val programImageFileId: Long?,
    val status: ProgramStatus,
    val title: String,
    val description: String?,
    val detailContent: String?,
    val applyStartAt: LocalDateTime,
    val applyEndAt: LocalDateTime,
    val programStartAt: LocalDateTime,
    val programEndAt: LocalDateTime,
    val location: String?,
    val currentAppliedUserCount: Int,
    val maxUserCount: Int,
    val contactNumber: String?,
    val attachmentFileIds: List<Long> = emptyList(),
    val lectures: List<String>,
    @Schema(description = "주관식 질문들")
    val freeQuestions: List<ProgramQuestionResponse>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(program: Program): ProgramInfoResponse {
            return ProgramInfoResponse(
                programId = program.id,
                programImageFileId = program.programImageFileId,
                status = program.status,
                title = program.title,
                description = program.description,
                detailContent = program.detailContent,
                applyStartAt = program.applyStartAt,
                applyEndAt = program.applyEndAt,
                programStartAt = program.programStartAt,
                programEndAt = program.programEndAt,
                location = program.location,
                currentAppliedUserCount = program.currentAppliedUserCount,
                maxUserCount = program.maxUserCount,
                contactNumber = program.contact,
                attachmentFileIds = program.attachmentFileIds,
                lectures = program.lectures,
                freeQuestions = program.freeQuestions.map { ProgramQuestionResponse.from(it) },
                createdAt = program.createdAt,
                updatedAt = program.updatedAt,
            )
        }
    }
}
