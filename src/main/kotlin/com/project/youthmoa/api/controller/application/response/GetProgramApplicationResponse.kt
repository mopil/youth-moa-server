package com.project.youthmoa.api.controller.application.response

import com.project.youthmoa.api.controller.program.response.ProgramInfoResponse
import com.project.youthmoa.api.controller.user.response.UserInfoResponse
import com.project.youthmoa.domain.model.ProgramApplication
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import java.time.LocalDateTime

data class GetProgramApplicationResponse(
    val applicationId: Long,
    val programInfo: ProgramInfoResponse,
    val applierInfo: UserInfoResponse,
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
                applicationId = application.id,
                programInfo = ProgramInfoResponse.from(application.program),
                applierInfo = UserInfoResponse.from(application.applier),
                answers = application.answers.map { QuestionAnswerResponse.of(it.question, it.answer) },
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
