package com.project.youthmoa.api.dto.response

import com.project.youthmoa.domain.model.Program
import com.project.youthmoa.domain.type.ProgramStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class ProgramSimpleResponse(
    val id: Long,
    val title: String,
    val status: ProgramStatus,
    @Schema(description = "yyyy-MM-dd 포맷")
    val applyStartDate: LocalDate,
    @Schema(description = "yyyy-MM-dd 포맷")
    val applyEndDate: LocalDate,
) {
    companion object {
        fun from(program: Program) =
            ProgramSimpleResponse(
                id = program.id,
                title = program.title,
                status = program.status,
                applyStartDate = program.applyStartDate,
                applyEndDate = program.applyEndDate,
            )
    }
}
