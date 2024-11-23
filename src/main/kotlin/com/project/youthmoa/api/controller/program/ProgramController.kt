package com.project.youthmoa.api.controller.program

import com.project.youthmoa.api.controller.program.request.CreateOrUpdateProgramRequest
import com.project.youthmoa.api.controller.program.request.GetAllProgramsRequest
import com.project.youthmoa.api.controller.program.response.ProgramInfoResponse
import com.project.youthmoa.api.controller.util.response.PageResponse
import com.project.youthmoa.domain.repository.ProgramRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.repository.spec.GetAllProgramsSpec
import com.project.youthmoa.domain.service.ProgramService
import org.springframework.web.bind.annotation.*

@RestController
class ProgramController(
    private val programService: ProgramService,
    private val programRepository: ProgramRepository,
) : ProgramApiDescription {
    @PostMapping("/admin/programs")
    fun registerProgram(
        @RequestBody request: CreateOrUpdateProgramRequest,
    ): List<Long> {
        return programService.createPrograms(request)
    }

    @GetMapping(
        "/api/programs",
        "/admin/programs",
    )
    override fun getAllPrograms(request: GetAllProgramsRequest): PageResponse<ProgramInfoResponse> {
        val page = programRepository.findAllBySpec(GetAllProgramsSpec.from(request))
        val content = page.content.map { ProgramInfoResponse.from(it!!) }
        return PageResponse.of(page, content)
    }

    @GetMapping(
        "/api/programs/{programId}",
        "/admin/programs/{programId}",
    )
    override fun getProgramDetailInfo(
        @PathVariable programId: Long,
    ): ProgramInfoResponse {
        return programRepository.findByIdOrThrow(programId)
            .let { ProgramInfoResponse.from(it) }
    }

    @PutMapping("/admin/programs/{programId}")
    fun updateProgram(
        @PathVariable programId: Long,
        @RequestBody request: CreateOrUpdateProgramRequest,
    ) {
        programService.updateProgram(programId, request)
    }

    @DeleteMapping("/admin/programs/{programId}")
    fun deleteProgram(
        @PathVariable programId: Long,
    ) {
        programService.deleteProgram(programId)
    }
}
