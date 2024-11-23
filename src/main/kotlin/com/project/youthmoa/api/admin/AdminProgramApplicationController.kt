package com.project.youthmoa.api.admin

import com.project.youthmoa.api.admin.response.GetProgramApplicationResponse
import com.project.youthmoa.domain.repository.ProgramApplicationRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/applications")
class AdminProgramApplicationController(
    private val programApplicationRepository: ProgramApplicationRepository,
) {
    @Operation(summary = "관리자 - 프로그램 신청서 조회")
    @GetMapping("/{applicationId}")
    fun getApplication(
        @PathVariable applicationId: Long,
    ): GetProgramApplicationResponse {
        val application = programApplicationRepository.findByIdOrThrow(applicationId)
        return GetProgramApplicationResponse.from(application)
    }
}
