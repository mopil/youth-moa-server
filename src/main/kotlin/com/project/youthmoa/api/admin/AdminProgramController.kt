package com.project.youthmoa.api.admin

import com.project.youthmoa.api.admin.request.CreateProgramRequest
import com.project.youthmoa.domain.service.ProgramService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/programs")
class AdminProgramController(
    private val programService: ProgramService,
) {
    @PostMapping
    fun createProgram(
        @RequestBody request: CreateProgramRequest,
    ): Long {
        return programService.createProgram(request)
    }

    @PutMapping("/{programId}")
    fun updateProgram(
        @PathVariable programId: Long,
        @RequestBody request: CreateProgramRequest,
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
