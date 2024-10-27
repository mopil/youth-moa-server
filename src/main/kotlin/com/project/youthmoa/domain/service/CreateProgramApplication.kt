package com.project.youthmoa.domain.service

import com.project.youthmoa.api.app.request.CreateProgramApplicationRequest
import com.project.youthmoa.common.util.FileManager
import com.project.youthmoa.domain.model.Program
import com.project.youthmoa.domain.model.ProgramApplication
import com.project.youthmoa.domain.model.User
import com.project.youthmoa.domain.repository.*
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

fun interface CreateProgramApplication {
    operator fun invoke(
        userId: Long,
        request: CreateProgramApplicationRequest,
    ): Long

    @Component
    class Default(
        private val fileManager: FileManager,
        private val userRepository: UserRepository,
        private val programRepository: ProgramRepository,
        private val programApplicationRepository: ProgramApplicationRepository,
    ) : CreateProgramApplication {
        @Transactional
        override fun invoke(
            userId: Long,
            request: CreateProgramApplicationRequest,
        ): Long {
            checkBeforeApplicationStatusIfExist(request.programId, userId)
            fileManager.checkExistence(request.attachmentFileIds)

            val program = programRepository.findByIdOrThrow(request.programId)
            val user = userRepository.findByIdOrThrow(userId)

            val application =
                if (program.isNeedAdminApprove.not()) {
                    program.addAppliedUserCount()
                    createAutoApprovedApplication(program, user, request)
                } else {
                    createApproveWaitingApplication(program, user, request)
                }

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

        private fun createApproveWaitingApplication(
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
                status = ProgramApplicationStatus.대기,
                applier = loginUser,
            ).let(programApplicationRepository::save)
        }

        private fun createAutoApprovedApplication(
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
                status = ProgramApplicationStatus.승인,
                adminComment = "자동 승인",
                adminActionDateTime = LocalDateTime.now(),
                applier = loginUser,
            ).let(programApplicationRepository::save)
        }
    }
}
