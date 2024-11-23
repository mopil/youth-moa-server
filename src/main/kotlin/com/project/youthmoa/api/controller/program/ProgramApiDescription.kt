package com.project.youthmoa.api.controller.program

import com.project.youthmoa.api.controller.program.request.GetAllProgramsRequest
import com.project.youthmoa.api.controller.program.response.ProgramInfoResponse
import com.project.youthmoa.api.controller.util.response.ErrorResponse
import com.project.youthmoa.api.controller.util.response.PageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject

@Tag(name = "프로그램")
interface ProgramApiDescription {
    @Operation(summary = "프로그램 목록 조회")
    fun getAllPrograms(
        @ParameterObject request: GetAllProgramsRequest,
    ): PageResponse<ProgramInfoResponse>

    @Operation(summary = "프로그램 상세 조회")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "404",
                description = "[errorType:NOT_FOUND] 존재하지 않는 프로그램",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun getProgramDetailInfo(programId: Long): ProgramInfoResponse
}
