package com.project.youthmoa.api.dto.response

import com.project.youthmoa.domain.model.ApplicationStatus
import com.project.youthmoa.domain.model.ProgramApplication
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
                applicationHistories = applicationHistories.map(GetUserApplicationHistory::from),
            )
    }
}

data class GetUserApplicationHistory(
    val programId: Long,
    val programTitle: String,
    val programImageUrl: String?,
    val applierName: String,
    val applierPhone: String,
    val applierEmail: String,
    val applierAddress: String,
    val applicationStatus: ApplicationStatus,
    val applicationDate: String,
    val applicationId: Long,
    val applicationCreatedAt: LocalDateTime,
    val applicationUpdatedAt: LocalDateTime,
    val cancelDateTime: LocalDateTime?,
    val cancelReason: String?,
    val approvedDateTime: LocalDateTime?,
    val rejectedDateTime: LocalDateTime?,
    val adminComment: String?,
) {
    companion object {
        fun from(programApplication: ProgramApplication): GetUserApplicationHistory {
            val approvedDateTime =
                if (programApplication.status == ApplicationStatus.승인) {
                    programApplication.adminActionDateTime
                } else {
                    null
                }
            val rejectedDateTime =
                if (programApplication.status == ApplicationStatus.반려) {
                    programApplication.adminActionDateTime
                } else {
                    null
                }
            return GetUserApplicationHistory(
                programId = programApplication.program.id,
                programTitle = programApplication.program.title,
                programImageUrl = programApplication.program.programImageUrl,
                applierName = programApplication.applierName,
                applierPhone = programApplication.applierPhone,
                applierEmail = programApplication.applierEmail,
                applierAddress = programApplication.applierAddress,
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
            )
        }
    }
}
