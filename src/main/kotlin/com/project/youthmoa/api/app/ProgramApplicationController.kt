package com.project.youthmoa.api.app

import com.project.youthmoa.api.app.request.CancelProgramApplicationRequest
import com.project.youthmoa.api.app.request.CreateProgramApplicationRequest
import com.project.youthmoa.api.app.response.GetUserApplicationHistoriesResponse
import com.project.youthmoa.api.app.spec.ProgramApplicationApiSpec
import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.common.util.AuthManager
import com.project.youthmoa.domain.service.CancelProgramApplication
import com.project.youthmoa.domain.service.CreateProgramApplication
import com.project.youthmoa.domain.service.ProgramApplicationService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/applications")
class ProgramApplicationController(
    private val programApplicationService: ProgramApplicationService,
    private val createProgramApplication: CreateProgramApplication,
    private val cancelProgramApplication: CancelProgramApplication,
    private val authManager: AuthManager,
) : ProgramApplicationApiSpec {
    @AuthenticationRequired
    @GetMapping
    override fun getUserProgramHistories(): GetUserApplicationHistoriesResponse {
        val loginUser = authManager.getCurrentLoginUser()
        return programApplicationService.getUserApplicationHistories(loginUser.id)
    }

    @AuthenticationRequired
    @PostMapping
    override fun createApplication(
        @Valid @RequestBody request: CreateProgramApplicationRequest,
    ): Long {
        val loginUser = authManager.getCurrentLoginUser()
        return createProgramApplication(loginUser.id, request)
    }

    @AuthenticationRequired
    @PutMapping("/{applicationId}/cancel")
    override fun cancelApplication(
        @PathVariable applicationId: Long,
        @Valid @RequestBody request: CancelProgramApplicationRequest,
    ) {
        val loginUser = authManager.getCurrentLoginUser()
        cancelProgramApplication(loginUser.id, applicationId, request)
    }
}
