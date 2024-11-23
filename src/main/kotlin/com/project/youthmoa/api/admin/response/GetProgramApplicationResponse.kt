package com.project.youthmoa.api.admin.response

import com.project.youthmoa.api.common.response.UserResponse
import com.project.youthmoa.domain.model.Program
import com.project.youthmoa.domain.model.ProgramApplication
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class GetProgramApplicationResponse(
    val programInfo: ProgramInfo,
    val applierInfo: UserResponse,
    val answers: List<QuestionAnswerResponse>,
    val attachmentFileIds: List<Long> = emptyList(),
    val status: ProgramApplicationStatus,
    val appliedAt: LocalDateTime,
    val approvedAt: LocalDateTime?,
    val rejectedAt: LocalDateTime?,
    val canceledAt: LocalDateTime?,
    val cancelReason: String?,
    val adminComment: String?,
) {
    companion object {
        fun from(application: ProgramApplication): GetProgramApplicationResponse {
            val rejectedAt = if (application.isRejected()) application.adminActionDateTime else null
            val canceledAt = if (application.isCanceled()) application.cancelDateTime else null
            val approvedAt = if (application.isApproved()) application.adminActionDateTime else null
            return GetProgramApplicationResponse(
                programInfo = ProgramInfo.from(application.program),
                applierInfo = UserResponse.from(application.applier),
                answers = application.answers.map { QuestionAnswerResponse(it.question.question, it.answer) },
                attachmentFileIds = application.attachmentFileIds,
                status = application.status,
                appliedAt = application.createdAt,
                approvedAt = approvedAt,
                rejectedAt = rejectedAt,
                canceledAt = canceledAt,
                cancelReason = application.cancelReason,
                adminComment = application.adminComment,
            )
        }
    }
}

data class QuestionAnswerResponse(
    val question: String,
    val answer: String,
)

data class ProgramInfo(
    val programImageFileId: Long?,
    val programTitle: String,
    val programStartDate: LocalDate,
    val programEndDate: LocalDate,
    val programStartAt: LocalDateTime?,
) {
    companion object {
        fun from(program: Program) =
            ProgramInfo(
                programImageFileId = program.programImageFileId,
                programTitle = program.title,
                programStartDate = program.programStartDate,
                programEndDate = program.programEndDate,
                programStartAt = program.programStartAt,
            )
    }
}
