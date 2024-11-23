package com.project.youthmoa.domain.service

import com.project.youthmoa.api.controller.application.request.CancelProgramApplicationRequest
import com.project.youthmoa.domain.model.ProgramApplication
import com.project.youthmoa.domain.repository.ProgramApplicationRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

fun interface CancelProgramApplication {
    operator fun invoke(
        userId: Long,
        applicationId: Long,
        request: CancelProgramApplicationRequest,
    )

    @Component
    class Default(
        private val programApplicationRepository: ProgramApplicationRepository,
    ) : CancelProgramApplication {
        @Transactional
        override fun invoke(
            userId: Long,
            applicationId: Long,
            request: CancelProgramApplicationRequest,
        ) {
            val application: ProgramApplication = programApplicationRepository.findByIdOrThrow(applicationId)
            application.cancelByUser(userId, request.cancelReason)
        }
    }
}
