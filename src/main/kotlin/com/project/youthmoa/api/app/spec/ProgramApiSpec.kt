package com.project.youthmoa.api.app.spec

import com.project.youthmoa.api.app.request.GetAllProgramsRequest
import com.project.youthmoa.api.app.response.ProgramDetailResponse
import com.project.youthmoa.api.app.response.ProgramSimpleResponse
import com.project.youthmoa.api.common.response.ErrorResponse
import com.project.youthmoa.api.common.response.PageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject

@Tag(name = "프로그램")
interface ProgramApiSpec {
    @Operation(summary = "프로그램 목록 조회")
    fun getAllPrograms(
        @ParameterObject request: GetAllProgramsRequest,
    ): PageResponse<ProgramSimpleResponse>

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
    fun getProgramDetailInfo(programId: Long): ProgramDetailResponse
}
