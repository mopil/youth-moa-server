package com.project.youthmoa.api.app.response

import com.project.youthmoa.domain.model.ProgramApplication
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import java.time.LocalDateTime

data class GetUserApplicationHistoriesResponse(
    val inProgressProgramCount: Int,
    val endProgramCount: Int,
    val applicationHistories: List<GetUserApplicationHistory>,
) {
    companion object {
        fun of(
            inProgressProgramCount: Int,
            endProgramCount: Int,
            applicationHistories: List<ProgramApplication>,
        ): GetUserApplicationHistoriesResponse =
            GetUserApplicationHistoriesResponse(
                inProgressProgramCount = inProgressProgramCount,
                endProgramCount = endProgramCount,
                applicationHistories = applicationHistories.map(GetUserApplicationHistory.Companion::from),
            )
    }
}

data class GetUserApplicationHistory(
    val programId: Long,
    val programTitle: String,
    val programImageFileId: Long?,
    val applierName: String,
    val applierPhone: String,
    val applierEmail: String,
    val applierAddress: String,
    val applicationStatus: ProgramApplicationStatus,
    val applicationDate: String,
    val applicationId: Long,
    val applicationCreatedAt: LocalDateTime,
    val applicationUpdatedAt: LocalDateTime,
    val cancelDateTime: LocalDateTime?,
    val cancelReason: String?,
    val approvedDateTime: LocalDateTime?,
    val rejectedDateTime: LocalDateTime?,
    val adminComment: String?,
    val attachmentFileIds: List<Long> = emptyList(),
) {
    companion object {
        fun from(programApplication: ProgramApplication): GetUserApplicationHistory {
            val approvedDateTime =
                if (programApplication.status == ProgramApplicationStatus.승인) {
                    programApplication.adminActionDateTime
                } else {
                    null
                }
            val rejectedDateTime =
                if (programApplication.status == ProgramApplicationStatus.반려) {
                    programApplication.adminActionDateTime
                } else {
                    null
                }
            return GetUserApplicationHistory(
                programId = programApplication.program.id,
                programTitle = programApplication.program.title,
                programImageFileId = programApplication.program.programImageFileId,
                applierName = programApplication.applier.name,
                applierPhone = programApplication.applier.phone,
                applierEmail = programApplication.applier.email,
                applierAddress = programApplication.applier.address,
                applicationStatus = programApplication.status,
                applicationDate = programApplication.createdAt.toLocalDate().toString(),
                applicationId = programApplication.id,
                applicationCreatedAt = programApplication.createdAt,
                applicationUpdatedAt = programApplication.updatedAt,
                cancelDateTime = programApplication.cancelDateTime,
                cancelReason = programApplication.cancelReason,
                approvedDateTime = approvedDateTime,
                rejectedDateTime = rejectedDateTime,
                adminComment = programApplication.adminComment,
                attachmentFileIds = programApplication.attachmentFileIds,
            )
        }
    }
}
