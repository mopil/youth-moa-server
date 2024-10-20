package com.project.youthmoa.domain.service

import com.project.youthmoa.api.app.response.GetUserApplicationHistoriesResponse
import com.project.youthmoa.common.auth.AuthenticationUtils
import com.project.youthmoa.domain.repository.ProgramApplicationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ProgramApplicationService {
    fun getUserApplicationHistories(): GetUserApplicationHistoriesResponse
}

@Service
@Transactional(readOnly = true)
class ProgramApplicationServiceImpl(
    private val programApplicationRepository: ProgramApplicationRepository,
) : ProgramApplicationService {
    override fun getUserApplicationHistories(): GetUserApplicationHistoriesResponse {
        val loginUser = AuthenticationUtils.getCurrentLoginUser()
        val applicationHistories = programApplicationRepository.findAllByApplierId(loginUser.id)

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
