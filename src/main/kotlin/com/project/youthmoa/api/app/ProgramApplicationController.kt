package com.project.youthmoa.api.app

import com.project.youthmoa.api.app.request.CancelProgramApplicationRequest
import com.project.youthmoa.api.app.request.CreateProgramApplicationRequest
import com.project.youthmoa.api.app.response.GetUserApplicationHistoriesResponse
import com.project.youthmoa.api.app.spec.ProgramApplicationApiSpec
import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.domain.service.CancelProgramApplication
import com.project.youthmoa.domain.service.CreateProgramApplication
import com.project.youthmoa.domain.service.ProgramApplicationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/applications")
class ProgramApplicationController(
    private val programApplicationService: ProgramApplicationService,
    private val createProgramApplication: CreateProgramApplication,
    private val cancelProgramApplication: CancelProgramApplication,
) : ProgramApplicationApiSpec {
    @AuthenticationRequired
    @GetMapping
    override fun getUserProgramHistories(): GetUserApplicationHistoriesResponse {
        return programApplicationService.getUserApplicationHistories()
    }

    @AuthenticationRequired
    @PostMapping
    override fun createApplication(
        @RequestBody request: CreateProgramApplicationRequest,
    ): Long {
        return createProgramApplication(request)
    }

    @AuthenticationRequired
    @PutMapping("/{applicationId}/cancel")
    override fun cancelApplication(
        @PathVariable applicationId: Long,
        @RequestBody request: CancelProgramApplicationRequest,
    ) {
        cancelProgramApplication(applicationId, request)
    }
}
