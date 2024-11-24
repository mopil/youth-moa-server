package com.project.youthmoa.api.controller.program.response

import com.project.youthmoa.domain.model.Program

data class ProgramCountResponse(
    val inProgressProgramCount: Int,
    val endProgramCount: Int,
) {
    companion object {
        fun from(programs: List<Program>): ProgramCountResponse {
            val inProgressProgramCount = programs.count { it.isInProgress() }
            val endProgramCount = programs.count { it.isEnd() }
            return ProgramCountResponse(
                inProgressProgramCount = inProgressProgramCount,
                endProgramCount = endProgramCount,
            )
        }
    }
}
