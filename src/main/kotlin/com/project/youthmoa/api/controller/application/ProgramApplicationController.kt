package com.project.youthmoa.api.controller.application

import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.api.controller.application.request.AdminUpdateApplicationRequest
import com.project.youthmoa.api.controller.application.request.ApplyApplicationRequest
import com.project.youthmoa.api.controller.application.request.CancelProgramApplicationRequest
import com.project.youthmoa.api.controller.application.response.GetAllApplicationsByUserResponse
import com.project.youthmoa.api.controller.application.response.GetAppliedProgramApplicationResponse
import com.project.youthmoa.api.controller.application.response.GetProgramApplicationResponse
import com.project.youthmoa.api.controller.common.response.PageResponse
import com.project.youthmoa.api.controller.program.response.ProgramCountResponse
import com.project.youthmoa.common.util.AuthManager
import com.project.youthmoa.common.util.file.ApplicationListExcelRow
import com.project.youthmoa.common.util.file.ExcelManager
import com.project.youthmoa.common.util.file.ExcelManager.Default.setExcelDownload
import com.project.youthmoa.domain.repository.ProgramApplicationRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.service.ProgramApplicationWriteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.joda.time.LocalDate
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@Tag(name = "프로그램 신청서")
@RestController
class ProgramApplicationController(
    private val programApplicationWriteService: ProgramApplicationWriteService,
    private val programApplicationRepository: ProgramApplicationRepository,
    private val authManager: AuthManager,
) : ProgramApplicationApiDescription {
    @Operation(summary = "인증된 사용자의 프로그램 신청 이력 조회 (카운트 조회 포함)")
    @AuthenticationRequired
    @GetMapping("/api/applications/by-me")
    fun getAllApplicationsByUser(): GetAllApplicationsByUserResponse {
        val loginUser = authManager.getCurrentLoginUser()
        val applications = programApplicationRepository.findAllByApplierId(loginUser.id)
        val programs = applications.map { it.program }

        return GetAllApplicationsByUserResponse(
            ProgramCountResponse.from(programs),
            applications = applications.map { GetProgramApplicationResponse.from(it) },
        )
    }

    @Operation(summary = "프로그램 참가 신청")
    @AuthenticationRequired
    @PostMapping("/api/applications")
    override fun applyApplication(
        @Valid @RequestBody request: ApplyApplicationRequest,
    ): Long {
        val loginUser = authManager.getCurrentLoginUser()
        return programApplicationWriteService.applyApplication(
            userId = loginUser.id,
            programId = request.programId,
            attachmentFileIds = request.attachmentFileIds,
            questionAnswers = request.questionAnswers,
        )
    }

    @Operation(summary = "프로그램 신청 취소")
    @AuthenticationRequired
    @PutMapping("/api/applications/{applicationId}/cancel")
    override fun cancelApplication(
        @PathVariable applicationId: Long,
        @Valid @RequestBody request: CancelProgramApplicationRequest,
    ) {
        val loginUser = authManager.getCurrentLoginUser()
        programApplicationWriteService.cancelApplicationByUser(
            userId = loginUser.id,
            applicationId = applicationId,
            cancelReason = request.cancelReason,
        )
    }

    @Operation(summary = "프로그램 신청서 상세 조회")
    @GetMapping("/api/applications/{applicationId}")
    fun getApplication(
        @PathVariable applicationId: Long,
    ): GetProgramApplicationResponse {
        val application = programApplicationRepository.findByIdOrThrow(applicationId)
        return GetProgramApplicationResponse.from(application)
    }

    @AuthenticationRequired
    @Operation(summary = "프로그램 신청서 상태 변경")
    @PutMapping("/admin/applications/{applicationId}")
    fun updateApplicationStatus(
        @PathVariable applicationId: Long,
        @Valid @RequestBody request: AdminUpdateApplicationRequest,
    ) {
        val loginAdmin = authManager.getCurrentLoginAdmin()
        programApplicationWriteService.updateApplicationByAdmin(
            applicationId = applicationId,
            adminUserId = loginAdmin.id,
            adminComment = request.adminComment,
            applicationStatus = request.applicationStatus,
        )
    }

    @Operation(summary = "프로그램 신청서 목록(현황) 조회")
    @GetMapping("/admin/applications")
    fun getAppliedProgramApplications(
        @RequestParam programId: Long,
        @RequestParam page: Int,
        @RequestParam size: Int,
    ): PageResponse<GetAppliedProgramApplicationResponse> {
        return programApplicationRepository.findAllByProgramIdAndAppliedCount(programId, PageRequest.of(page, size))
            .map { GetAppliedProgramApplicationResponse.from(it) }
            .let { PageResponse.from(it) }
    }

    @Operation(summary = "프로그램 신청서 목록(현황) 엑셀 다운로드")
    @GetMapping("/admin/applications/download/excel")
    fun downloadApplicationsExcel(
        @RequestParam programId: Long,
        response: HttpServletResponse,
    ) {
        val applications = programApplicationRepository.findAllByProgramId(programId)
        val program = applications.first().program
        val workbook =
            ExcelManager.Default.createExcelWorkbook(
                sheetName = "[${program.title}] 신청서 목록",
                dataList = applications.map { ApplicationListExcelRow.from(it) },
            )
        response.setExcelDownload("program_${programId}_application_list(${LocalDate.now()}).xlsx", workbook)
    }
}
