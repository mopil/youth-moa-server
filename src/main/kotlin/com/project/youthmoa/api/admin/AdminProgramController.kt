package com.project.youthmoa.api.admin

import com.project.youthmoa.api.admin.request.CreateOrUpdateProgramRequest
import com.project.youthmoa.domain.service.ProgramService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/programs")
class AdminProgramController(
    private val programService: ProgramService,
) {
    @PostMapping
    fun createProgram(
        @RequestBody request: CreateOrUpdateProgramRequest,
    ): List<Long> {
        return programService.createPrograms(request)
    }

    @PutMapping("/{programId}")
    fun updateProgram(
        @PathVariable programId: Long,
        @RequestBody request: CreateOrUpdateProgramRequest,
    ) {
        programService.updateProgram(programId, request)
    }

    @DeleteMapping("/{programId}")
    fun deleteProgram(
        @PathVariable programId: Long,
    ) {
        programService.deleteProgram(programId)
    }
}
