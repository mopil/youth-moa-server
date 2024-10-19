package com.project.youthmoa.api.app

import com.project.youthmoa.api.app.spec.ProgramApplicationApiSpec
import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.api.dto.request.CancelProgramApplicationRequest
import com.project.youthmoa.api.dto.request.CreateProgramApplicationRequest
import com.project.youthmoa.api.dto.response.GetUserApplicationHistoriesResponse
import com.project.youthmoa.domain.service.ProgramApplicationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/applications")
class ProgramApplicationController(
    private val programApplicationService: ProgramApplicationService,
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
        return programApplicationService.createProgramApplication(request)
    }

    @AuthenticationRequired
    @PutMapping("/{applicationId}/cancel")
    override fun cancelApplication(
        @PathVariable applicationId: Long,
        @RequestBody request: CancelProgramApplicationRequest,
    ) {
        programApplicationService.cancelProgramApplication(applicationId, request)
    }
}
