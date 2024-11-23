package com.project.youthmoa.domain.service

import com.project.youthmoa.api.controller.application.response.GetUserApplicationHistoriesResponse
import com.project.youthmoa.domain.repository.ProgramApplicationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ProgramApplicationService {
    fun getUserApplicationHistories(userId: Long): GetUserApplicationHistoriesResponse
}

@Service
@Transactional(readOnly = true)
class ProgramApplicationServiceImpl(
    private val programApplicationRepository: ProgramApplicationRepository,
) : ProgramApplicationService {
    override fun getUserApplicationHistories(userId: Long): GetUserApplicationHistoriesResponse {
        val applicationHistories = programApplicationRepository.findAllByApplierId(userId)

        val inProgressProgramCount =
            applicationHistories.count {
                it.program.isInProgress() && (it.isWaiting() || it.isApproved())
            }
        val endProgramCount =
            applicationHistories.count {
                it.program.isEnd() && it.isApproved()
            }

        return GetUserApplicationHistoriesResponse.of(
            inProgressProgramCount = inProgressProgramCount,
            endProgramCount = endProgramCount,
            applicationHistories = applicationHistories,
        )
    }
}
