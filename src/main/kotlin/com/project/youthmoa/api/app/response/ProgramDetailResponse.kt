package com.project.youthmoa.api.app.response

import com.project.youthmoa.domain.model.Program
import com.project.youthmoa.domain.type.ProgramStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime

data class ProgramDetailResponse(
    val programId: Long,
    val programImageUrl: String?,
    val status: ProgramStatus,
    val title: String,
    val description: String?,
    val detailContent: String?,
    val applyStartDate: LocalDate,
    val applyEndDate: LocalDate,
    val programStartDate: LocalDate,
    val programEndDate: LocalDate,
    val location: String?,
    val maxUserCount: Int,
    val contactNumber: String?,
    val attachmentUrl: String?,
    val lectures: List<String>,
    @Schema(description = "주관식 질문들")
    val freeQuestions: List<String>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(program: Program): ProgramDetailResponse {
            return ProgramDetailResponse(
                programId = program.id,
                programImageUrl = program.programImageUrl,
                status = program.status,
                title = program.title,
                description = program.description,
                detailContent = program.detailContent,
                applyStartDate = program.applyStartDate,
                applyEndDate = program.applyEndDate,
                programStartDate = program.programStartDate,
                programEndDate = program.programEndDate,
                location = program.location,
                maxUserCount = program.maxUserCount,
                contactNumber = program.contactNumber,
                attachmentUrl = program.attachmentUrl,
                lectures = program.lectures,
                freeQuestions = program.freeQuestions.map { it.question },
                createdAt = program.createdAt,
                updatedAt = program.updatedAt,
            )
        }
    }
}
