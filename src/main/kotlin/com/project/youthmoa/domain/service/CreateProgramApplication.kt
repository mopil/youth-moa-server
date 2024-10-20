package com.project.youthmoa.domain.service

import com.project.youthmoa.api.app.request.CreateProgramApplicationRequest
import com.project.youthmoa.common.auth.AuthenticationUtils
import com.project.youthmoa.domain.model.Program
import com.project.youthmoa.domain.model.ProgramApplication
import com.project.youthmoa.domain.model.User
import com.project.youthmoa.domain.repository.ProgramApplicationRepository
import com.project.youthmoa.domain.repository.ProgramRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

fun interface CreateProgramApplication {
    operator fun invoke(request: CreateProgramApplicationRequest): Long

    @Component
    class Default(
        private val programRepository: ProgramRepository,
        private val programApplicationRepository: ProgramApplicationRepository,
    ) : CreateProgramApplication {
        @Transactional
        override fun invoke(request: CreateProgramApplicationRequest): Long {
            val loginUser = AuthenticationUtils.getCurrentLoginUser()
            val program = programRepository.findByIdOrThrow(request.programId)

            checkBeforeApplicationStatusIfExist(program.id, loginUser.id)

            if (program.isNeedApprove.not()) {
                program.addAppliedUserCount()
            }

            val application = createApplication(program, loginUser, request)
            return application.id
        }

        private fun checkBeforeApplicationStatusIfExist(
            programId: Long,
            loginUserId: Long,
        ) {
            val beforeApplication =
                programApplicationRepository.findByProgramIdAndApplierId(programId, loginUserId)
                    ?: return
            if (beforeApplication.isApproved()) {
                throw IllegalStateException("이미 승인 완료 되었습니다.")
            }
            if (beforeApplication.isWaiting()) {
                throw IllegalStateException("이미 신청하여 승인 대기중입니다.")
            }
        }

        private fun createApplication(
            program: Program,
            loginUser: User,
            request: CreateProgramApplicationRequest,
        ): ProgramApplication {
            return ProgramApplication(
                program = program,
                applierName = request.applierName,
                applierPhone = request.applierPhone,
                applierEmail = request.applierEmail,
                applierAddress = request.applierAddress,
                attachmentFileIds = request.attachmentFileIds,
                status = if (program.isNeedApprove) ProgramApplicationStatus.대기 else ProgramApplicationStatus.승인,
                applier = loginUser,
            ).let(programApplicationRepository::save)
        }
    }
}
