package com.project.youthmoa.api.controller.common

import com.project.youthmoa.api.controller.common.response.ErrorResponse
import com.project.youthmoa.api.controller.common.response.FileMetaResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

interface FileApiDescription {
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "400",
                description = "[errorType:BAD_REQUEST] 파일 업로드 중 에러 발생",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "401",
                description = "[errorType:UNAUTHORIZED, EXPIRED_TOKEN, INVALID_TOKEN] 인증 되어 있지 않음, 만료된 토큰, 잘못된 토큰",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "413",
                description = "[errorType:TOO_LARGE_FILE] 파일 크기 초과",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "429",
                description = "[errorType:TOO_MANY_REQUEST] 짧은 시간 동안 너무 많은 요청",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun uploadFile(file: MultipartFile): FileMetaResponse

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "404",
                description = "[errorType:NOT_FOUND] 존재하지 않는 파일",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun downloadFile(fileId: Long): ResponseEntity<InputStreamResource>
}
