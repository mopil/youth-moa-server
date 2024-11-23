package com.project.youthmoa.api.controller.user

import com.project.youthmoa.api.controller.user.request.*
import com.project.youthmoa.api.controller.user.response.FindEmailListResponse
import com.project.youthmoa.api.controller.user.response.UserEmailDuplicationCheckResponse
import com.project.youthmoa.api.controller.user.response.UserInfoResponse
import com.project.youthmoa.api.controller.user.response.UserLoginResponse
import com.project.youthmoa.api.controller.util.response.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject

@Tag(name = "회원")
interface UserApiDescription {
    @Operation(summary = "회원가입")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "400",
                description = "[errorType:BAD_REQUEST] 유효하지 않는 입력값",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun signUp(request: CreateUserRequest): UserLoginResponse

    @Operation(summary = "로그인")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "400",
                description = "[errorType:BAD_REQUEST] 유효하지 않는 입력값",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "401",
                description = "[errorType:INVALID_PASSWORD] 비밀번호 일치하지 않음",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "[errorType:NOT_FOUND] 존재하지 않는 ID(이메일)",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun login(request: UserLoginRequest): UserLoginResponse

    @Operation(summary = "아이디(이메일) 중복 확인")
    fun checkEmailDuplication(email: String): UserEmailDuplicationCheckResponse

    @Operation(summary = "이름, 휴대폰 번호로 아이디(이메일) 찾기")
    fun findAllEmailByNameAndPhone(
        @ParameterObject request: FindEmailRequest,
    ): FindEmailListResponse

    @Operation(summary = "비밀번호 재설정 (이메일로 임시 비밀번호 발급)")
    fun resetPassword(request: ResetPasswordRequest)

    @Operation(summary = "사용자 정보 수정")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "401",
                description = "[errorType:UNAUTHORIZED, EXPIRED_TOKEN, INVALID_TOKEN] 인증 되어 있지 않음, 만료된 토큰, 잘못된 토큰",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "403",
                description = "[errorType:FORBIDDEN] 본인만 수정 가능함 (인증된 사용자와 수정하려는 사용자가 다름)",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "[errorType:NOT_FOUND] 존재하지 않는 유저",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun updateUserInfo(
        userId: Long,
        request: UpdateUserInfoRequest,
    ): UserInfoResponse

    @Operation(summary = "회원탈퇴")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "401",
                description = "[errorType:UNAUTHORIZED, EXPIRED_TOKEN, INVALID_TOKEN] 인증 되어 있지 않음, 만료된 토큰, 잘못된 토큰",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "403",
                description = "[errorType:FORBIDDEN] 본인만 탈퇴 가능함 (인증된 사용자와 탈퇴하려는 사용자가 다름)",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "[errorType:NOT_FOUND] 존재하지 않는 유저",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun withdraw(userId: Long)
}
