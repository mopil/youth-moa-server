package com.project.youthmoa.api.controller.application

import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.api.controller.application.request.CancelProgramApplicationRequest
import com.project.youthmoa.api.controller.application.request.CreateProgramApplicationRequest
import com.project.youthmoa.api.controller.application.response.GetAppliedProgramApplicationResponse
import com.project.youthmoa.api.controller.application.response.GetProgramApplicationResponse
import com.project.youthmoa.api.controller.application.response.GetUserApplicationHistoriesResponse
import com.project.youthmoa.api.controller.common.response.PageResponse
import com.project.youthmoa.common.util.AuthManager
import com.project.youthmoa.domain.repository.ProgramApplicationRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.service.CancelProgramApplication
import com.project.youthmoa.domain.service.CreateProgramApplication
import com.project.youthmoa.domain.service.ProgramApplicationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@Tag(name = "프로그램 신청서")
@RestController
class ProgramApplicationController(
    private val programApplicationService: ProgramApplicationService,
    private val programApplicationRepository: ProgramApplicationRepository,
    private val createProgramApplication: CreateProgramApplication,
    private val cancelProgramApplication: CancelProgramApplication,
    private val authManager: AuthManager,
) : ProgramApplicationApiDescription {
    @Operation(summary = "인증된 사용자의 프로그램 신청 이력 조회 (카운트 조회 포함)")
    @AuthenticationRequired
    @GetMapping("/api/applications")
    fun getUserProgramHistories(): GetUserApplicationHistoriesResponse {
        val loginUser = authManager.getCurrentLoginUser()
        return programApplicationService.getUserApplicationHistories(loginUser.id)
    }

    @Operation(summary = "프로그램 참가 신청")
    @AuthenticationRequired
    @PostMapping("/api/applications")
    override fun createApplication(
        @Valid @RequestBody request: CreateProgramApplicationRequest,
    ): Long {
        val loginUser = authManager.getCurrentLoginUser()
        return createProgramApplication(loginUser.id, request)
    }

    @Operation(summary = "프로그램 신청 취소")
    @AuthenticationRequired
    @PutMapping("/api/applications/{applicationId}/cancel")
    override fun cancelApplication(
        @PathVariable applicationId: Long,
        @Valid @RequestBody request: CancelProgramApplicationRequest,
    ) {
        val loginUser = authManager.getCurrentLoginUser()
        cancelProgramApplication(loginUser.id, applicationId, request)
    }

    @Operation(summary = "프로그램 신청서 상세 조회")
    @GetMapping("/api/applications/{applicationId}")
    fun getApplication(
        @PathVariable applicationId: Long,
    ): GetProgramApplicationResponse {
        val application = programApplicationRepository.findByIdOrThrow(applicationId)
        return GetProgramApplicationResponse.from(application)
    }

    @Operation(summary = "프로그램 신청서 목록(현황) 조회")
    @GetMapping("/admin/applications")
    fun getAppliedProgramApplications(
        @RequestParam programId: Long,
        @RequestParam page: Int,
        @RequestParam size: Int,
    ): PageResponse<GetAppliedProgramApplicationResponse> {
        return programApplicationRepository.findAllByProgramId(programId, PageRequest.of(page, size))
            .map { GetAppliedProgramApplicationResponse.from(it) }
            .let { PageResponse.from(it) }
    }
}
