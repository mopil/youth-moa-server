package com.project.youthmoa.domain.service

import com.project.youthmoa.api.app.request.CancelProgramApplicationRequest
import com.project.youthmoa.common.auth.AuthenticationUtils
import com.project.youthmoa.common.exception.ForbiddenException
import com.project.youthmoa.domain.model.ProgramApplication
import com.project.youthmoa.domain.repository.ProgramApplicationRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

fun interface CancelProgramApplication {
    operator fun invoke(
        applicationId: Long,
        request: CancelProgramApplicationRequest,
    )

    @Component
    class Default(
        private val programApplicationRepository: ProgramApplicationRepository,
    ) : CancelProgramApplication {
        @Transactional
        override fun invoke(
            applicationId: Long,
            request: CancelProgramApplicationRequest,
        ) {
            val loginUser = AuthenticationUtils.getCurrentLoginUser()
            val application: ProgramApplication = programApplicationRepository.findByIdOrThrow(applicationId)

            if (application.isApproved()) {
                throw IllegalStateException("승인된 건은 취소할 수 없습니다.")
            }

            if (application.isRejected()) {
                throw IllegalStateException("반려된 건은 취소할 수 없습니다.")
            }

            if (application.applier.id != loginUser.id) {
                throw ForbiddenException("신청자만 취소할 수 있습니다.")
            }

            application.apply {
                status = ProgramApplicationStatus.취소
                cancelReason = request.cancelReason
                program.currentAppliedUserCount--
            }
        }
    }
}
