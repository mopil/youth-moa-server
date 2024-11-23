package com.project.youthmoa.api.controller.program

import com.project.youthmoa.api.controller.common.response.PageResponse
import com.project.youthmoa.api.controller.program.request.CreateOrUpdateProgramRequest
import com.project.youthmoa.api.controller.program.request.GetAllProgramsRequest
import com.project.youthmoa.api.controller.program.response.ProgramInfoResponse
import com.project.youthmoa.domain.repository.ProgramRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.repository.spec.GetAllProgramsSpec
import com.project.youthmoa.domain.service.ProgramService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "프로그램")
@RestController
class ProgramController(
    private val programService: ProgramService,
    private val programRepository: ProgramRepository,
) {
    @Operation(summary = "프로그램 등록")
    @PostMapping("/admin/programs")
    fun registerProgram(
        @RequestBody request: CreateOrUpdateProgramRequest,
    ): List<Long> {
        return programService.createPrograms(request)
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
    @PutMapping("/admin/programs/{programId}")
    fun updateProgram(
        @PathVariable programId: Long,
        @RequestBody request: CreateOrUpdateProgramRequest,
    ) {
        programService.updateProgram(programId, request)
    }

    @Operation(summary = "프로그램 삭제")
    @DeleteMapping("/admin/programs/{programId}")
    fun deleteProgram(
        @PathVariable programId: Long,
    ) {
        programService.deleteProgram(programId)
    }
}
