package com.project.youthmoa.api.controller.application.response

import com.project.youthmoa.api.controller.user.response.UserInfoResponse
import com.project.youthmoa.domain.model.ProgramApplication
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import java.time.LocalDateTime

data class GetAppliedProgramApplicationResponse(
    val applierInfo: UserInfoResponse,
    val appliedAt: LocalDateTime,
    val appliedCount: Long,
    val applicationStatus: ProgramApplicationStatus,
) {
    companion object {
        fun from(application: ProgramApplication): GetAppliedProgramApplicationResponse {
            return GetAppliedProgramApplicationResponse(
                applierInfo = UserInfoResponse.from(application.applier),
                appliedAt = application.createdAt,
                // TODO: 신청횟수 조회
                appliedCount = 0,
                applicationStatus = application.status,
            )
        }
    }
}
