package com.project.youthmoa.api.controller.application.response

import com.project.youthmoa.api.controller.user.response.UserInfoResponse
import com.project.youthmoa.domain.repository.dto.ProgramApplicationWithAppliedCount
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import java.time.LocalDateTime

data class GetAppliedProgramApplicationResponse(
    val applierInfo: UserInfoResponse,
    val appliedAt: LocalDateTime,
    val appliedCount: Long,
    val applicationStatus: ProgramApplicationStatus,
) {
    companion object {
        fun from(applicationWithCount: ProgramApplicationWithAppliedCount?): GetAppliedProgramApplicationResponse {
            require(applicationWithCount != null) { "신청 정보가 존재하지 않습니다." }
            return GetAppliedProgramApplicationResponse(
                applierInfo = UserInfoResponse.from(applicationWithCount.applier),
                appliedAt = applicationWithCount.appliedAt,
                appliedCount = applicationWithCount.appliedCount,
                applicationStatus = applicationWithCount.applicationStatus,
            )
        }
    }
}
