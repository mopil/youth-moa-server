package com.project.youthmoa.api.controller.application

import com.project.youthmoa.api.controller.application.request.CancelProgramApplicationRequest
import com.project.youthmoa.api.controller.application.request.CreateProgramApplicationRequest
import com.project.youthmoa.api.controller.application.response.GetUserApplicationHistoriesResponse
import com.project.youthmoa.api.controller.util.response.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "프로그램 신청서")
interface ProgramApplicationApiDescription {
    @Operation(summary = "인증된 사용자의 프로그램 신청 이력 조회 (카운트 조회 포함)")
    fun getUserProgramHistories(): GetUserApplicationHistoriesResponse

    @Operation(summary = "프로그램 참가 신청")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "400",
                description = "[errorType:BAD_REQUEST] 유효하지 않는 입력값, 이미 신청하여 상태가 대기/승인인 경우",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "401",
                description = "[errorType:UNAUTHORIZED, EXPIRED_TOKEN, INVALID_TOKEN] 인증 되어 있지 않음, 만료된 토큰, 잘못된 토큰",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "[errorType:NOT_FOUND] 존재하지 않는 프로그램",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun createApplication(request: CreateProgramApplicationRequest): Long

    @Operation(summary = "프로그램 신청 취소")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "400",
                description = "[errorType:BAD_REQUEST] 유효하지 않는 입력값, 상태가 승인/반려인 경우",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "401",
                description = "[errorType:UNAUTHORIZED, EXPIRED_TOKEN, INVALID_TOKEN] 인증 되어 있지 않음, 만료된 토큰, 잘못된 토큰",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "403",
                description = "[errorType:FORBIDDEN] 본인만 취소 가능함 (인증된 사용자와 취소하려는 사용자가 다름)",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "[errorType:NOT_FOUND] 존재하지 않는 신청서",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun cancelApplication(
        applicationId: Long,
        request: CancelProgramApplicationRequest,
    )
}
