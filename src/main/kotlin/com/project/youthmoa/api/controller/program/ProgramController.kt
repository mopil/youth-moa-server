package com.project.youthmoa.api.controller.program

import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.api.controller.common.response.PageResponse
import com.project.youthmoa.api.controller.program.request.CreateOrUpdateProgramRequest
import com.project.youthmoa.api.controller.program.request.GetAllProgramsRequest
import com.project.youthmoa.api.controller.program.response.GetAllProgramsByAdminResponse
import com.project.youthmoa.api.controller.program.response.ProgramCountResponse
import com.project.youthmoa.api.controller.program.response.ProgramInfoResponse
import com.project.youthmoa.common.util.AuthManager
import com.project.youthmoa.domain.repository.ProgramRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.repository.spec.GetAllProgramsSpec
import com.project.youthmoa.domain.service.ProgramWriteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "프로그램")
@RestController
class ProgramController(
    private val authManager: AuthManager,
    private val programWriteService: ProgramWriteService,
    private val programRepository: ProgramRepository,
) {
    @Operation(summary = "프로그램 등록")
    @AuthenticationRequired
    @PostMapping("/admin/programs")
    fun registerProgram(
        @RequestBody request: CreateOrUpdateProgramRequest,
    ): List<Long> {
        val loginAdmin = authManager.getCurrentLoginAdmin()
        return programWriteService.createPrograms(loginAdmin.id, request)
    }

    @Operation(summary = "프로그램 목록 조회")
    @GetMapping(
        "/api/programs",
        "/admin/programs",
    )
    fun getAllPrograms(request: GetAllProgramsRequest): PageResponse<ProgramInfoResponse> {
        val page = programRepository.findAllBySpec(GetAllProgramsSpec.from(request))
        val content = page.content.map { ProgramInfoResponse.from(it!!) }
        return PageResponse.of(page, content)
    }

    @Operation(summary = "현재 로그인한 관리자가 등록한 프로그램 목록 조회")
    @GetMapping(
        "/admin/programs/by-me",
    )
    fun getAllProgramsRegisterByAdmin(): GetAllProgramsByAdminResponse {
        val loginAdmin = authManager.getCurrentLoginAdmin()
        val programs = programRepository.findAllByAdminUserId(loginAdmin.id)
        return GetAllProgramsByAdminResponse(
            countInfo = ProgramCountResponse.from(programs),
            programs = programs.map { ProgramInfoResponse.from(it) },
        )
    }

    @Operation(summary = "프로그램 상세 조회")
    @GetMapping(
        "/api/programs/{programId}",
        "/admin/programs/{programId}",
    )
    fun getProgramDetailInfo(
        @PathVariable programId: Long,
    ): ProgramInfoResponse {
        return programRepository.findByIdOrThrow(programId)
            .let { ProgramInfoResponse.from(it) }
    }

    @Operation(summary = "프로그램 수정")
    @AuthenticationRequired
    @PutMapping("/admin/programs/{programId}")
    fun updateProgram(
        @PathVariable programId: Long,
        @RequestBody request: CreateOrUpdateProgramRequest,
    ) {
        val loginAdmin = authManager.getCurrentLoginAdmin()
        programWriteService.updateProgram(programId, loginAdmin.id, request)
    }

    @Operation(summary = "프로그램 삭제")
    @AuthenticationRequired
    @DeleteMapping("/admin/programs/{programId}")
    fun deleteProgram(
        @PathVariable programId: Long,
    ) {
        val loginAdmin = authManager.getCurrentLoginAdmin()
        programWriteService.deleteProgram(programId, loginAdmin.id)
    }
}
