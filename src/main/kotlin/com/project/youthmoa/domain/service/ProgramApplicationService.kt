package com.project.youthmoa.domain.service

import com.project.youthmoa.api.dto.request.CancelProgramApplicationRequest
import com.project.youthmoa.api.dto.request.CreateProgramApplicationRequest
import com.project.youthmoa.api.dto.response.GetUserApplicationHistoriesResponse
import com.project.youthmoa.common.auth.AuthenticationUtils
import com.project.youthmoa.common.exception.ForbiddenException
import com.project.youthmoa.domain.model.ProgramApplication
import com.project.youthmoa.domain.repository.ProgramApplicationRepository
import com.project.youthmoa.domain.repository.ProgramRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import com.project.youthmoa.domain.type.ProgramStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ProgramApplicationService {
    fun getUserApplicationHistories(): GetUserApplicationHistoriesResponse

    fun createProgramApplication(request: CreateProgramApplicationRequest): Long

    fun cancelProgramApplication(
        applicationId: Long,
        request: CancelProgramApplicationRequest,
    )
}

@Service
@Transactional(readOnly = true)
class ProgramApplicationServiceImpl(
    private val programRepository: ProgramRepository,
    private val programApplicationRepository: ProgramApplicationRepository,
) : ProgramApplicationService {
    override fun getUserApplicationHistories(): GetUserApplicationHistoriesResponse {
        val loginUser = AuthenticationUtils.getCurrentLoginUser()
        val applicationHistories = programApplicationRepository.findAllByApplierId(loginUser.id)

        val inProgressProgramCount =
            applicationHistories.count {
                it.program.status == ProgramStatus.진행중 &&
                    it.status in listOf(ProgramApplicationStatus.승인, ProgramApplicationStatus.대기)
            }
        val endProgramCount =
            applicationHistories.count {
                it.program.status == ProgramStatus.종료 &&
                    it.status == ProgramApplicationStatus.승인
            }

        return GetUserApplicationHistoriesResponse.of(
            inProgressProgramCount = inProgressProgramCount,
            endProgramCount = endProgramCount,
            applicationHistories = applicationHistories,
        )
    }

    @Transactional
    override fun createProgramApplication(request: CreateProgramApplicationRequest): Long {
        val loginUser = AuthenticationUtils.getCurrentLoginUser()
        val program = programRepository.findByIdOrThrow(request.programId)

        programApplicationRepository.findByProgramIdAndApplierId(request.programId, loginUser.id)?.let {
            when (it.status) {
                ProgramApplicationStatus.승인 -> {
                    throw IllegalStateException("이미 승인 완료 되었습니다.")
                }

                ProgramApplicationStatus.대기 -> {
                    throw IllegalStateException("이미 신청하여 승인 대기중입니다.")
                }

                else -> {}
            }
        }

        val application =
            ProgramApplication(
                program = program,
                applierName = request.applierName,
                applierPhone = request.applierPhone.value,
                applierEmail = request.applierEmail.value,
                applierAddress = request.applierAddress,
                attachmentFileIds = request.attachmentFileIds,
                applier = loginUser,
            ).let(programApplicationRepository::save)

        return application.id
    }

    @Transactional
    override fun cancelProgramApplication(
        applicationId: Long,
        request: CancelProgramApplicationRequest,
    ) {
        val loginUser = AuthenticationUtils.getCurrentLoginUser()
        val application = programApplicationRepository.findByIdOrThrow(applicationId)

        if (application.status == ProgramApplicationStatus.승인) {
            throw IllegalStateException("승인된 건은 취소할 수 없습니다.")
        }

        if (application.status == ProgramApplicationStatus.반려) {
            throw IllegalStateException("반려된 건은 취소할 수 없습니다.")
        }

        if (application.applier.id != loginUser.id) {
            throw ForbiddenException("신청자만 취소할 수 있습니다.")
        }

        application.apply {
            status = ProgramApplicationStatus.취소
            cancelReason = request.cancelReason
        }
    }
}
