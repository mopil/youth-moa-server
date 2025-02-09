package com.project.youthmoa.api.controller.user

import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.api.controller.common.response.PageResponse
import com.project.youthmoa.api.controller.user.request.*
import com.project.youthmoa.api.controller.user.response.FindEmailListResponse
import com.project.youthmoa.api.controller.user.response.FindEmailResponse
import com.project.youthmoa.api.controller.user.response.UserEmailDuplicationCheckResponse
import com.project.youthmoa.api.controller.user.response.UserInfoResponse
import com.project.youthmoa.api.controller.user.response.UserLoginResponse
import com.project.youthmoa.common.util.AuthManager
import com.project.youthmoa.common.util.file.ExcelManager
import com.project.youthmoa.common.util.file.ExcelManager.Default.setExcelDownload
import com.project.youthmoa.common.util.file.UserListExcelRow
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.spec.GetAllUsersSpec
import com.project.youthmoa.domain.service.UserLoginService
import com.project.youthmoa.domain.service.UserWriteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Tag(name = "회원")
@RestController
class UserController(
    private val userLoginService: UserLoginService,
    private val userWriteService: UserWriteService,
    private val userRepository: UserRepository,
    private val authManager: AuthManager,
) : UserApiDescription {
    @Operation(summary = "회원가입")
    @PostMapping("/api/users/sign-up")
    override fun signUp(
        @Valid @RequestBody request: CreateUserRequest,
    ): UserLoginResponse {
        return userLoginService.signUp(request)
    }

    @Operation(summary = "로그인")
    @PostMapping(
        "/api/users/login",
        "/admin/users/login",
    )
    override fun login(
        @Valid @RequestBody request: UserLoginRequest,
    ): UserLoginResponse {
        return userLoginService.login(request)
    }

    @Operation(summary = "아이디(이메일) 중복 확인")
    @GetMapping("/api/users/email-duplication")
    fun checkEmailDuplication(
        @RequestParam email: String,
    ): UserEmailDuplicationCheckResponse {
        return if (userRepository.findByEmail(email) == null) {
            UserEmailDuplicationCheckResponse(false)
        } else {
            UserEmailDuplicationCheckResponse(true)
        }
    }

    @Operation(summary = "이름, 휴대폰 번호로 아이디(이메일) 찾기")
    @GetMapping(
        "/api/users/emails",
        "/admin/users/emails",
    )
    fun findAllEmailByNameAndPhone(request: FindEmailRequest) =
        userRepository.findAllByNameAndPhone(request.name, request.phone)
            .map { FindEmailResponse.from(it) }
            .let { FindEmailListResponse(it) }

    @Operation(summary = "비밀번호 재설정 (이메일로 임시 비밀번호 발급)")
    @PostMapping(
        "/api/users/reset-password",
        "/admin/users/reset-password",
    )
    fun resetPassword(
        @Valid @RequestBody request: ResetPasswordRequest,
    ) {
        userWriteService.resetPassword(request.email)
    }

    @Operation(summary = "사용자 정보 조회")
    @AuthenticationRequired
    @GetMapping("/admin/users/{userId}")
    fun getUserInfo(
        @PathVariable userId: Long,
    ): UserInfoResponse {
        return userRepository.findById(userId).map { UserInfoResponse.from(it) }
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다.") }
    }

    @Operation(summary = "사용자 정보 수정")
    @AuthenticationRequired
    @PutMapping(
        "/api/users/{userId}",
        "/admin/users/{userId}",
    )
    override fun updateUserInfo(
        @PathVariable userId: Long,
        @Valid @RequestBody request: UpdateUserInfoRequest,
    ): UserInfoResponse {
        return userWriteService.updateUserInfo(userId, request)
    }

    @Operation(summary = "회원탈퇴")
    @AuthenticationRequired
    @DeleteMapping("/api/users/{userId}")
    override fun withdraw(
        @PathVariable userId: Long,
    ) {
        authManager.checkIsSelf(userId)
        userWriteService.withdraw(userId)
    }

    @Operation(summary = "사용자 목록 조회")
    @GetMapping("/admin/users")
    fun getAllUsers(request: GetAllUsersRequest): PageResponse<UserInfoResponse> {
        val users = userRepository.findAllBySpec(GetAllUsersSpec.from(request))
        val content = users.content.mapNotNull { UserInfoResponse.from(it!!) }
        return PageResponse(
            totalCount = users.totalElements,
            isLast = users.isLast,
            totalPageCount = users.totalPages,
            content = content,
        )
    }

    @Operation(summary = "사용자 목록 엑셀 다운로드")
    @GetMapping("/admin/users/download/excel")
    fun downloadUserExcel(response: HttpServletResponse) {
        val users =
            userRepository.findAll().map {
                UserListExcelRow.from(it)
            }
        val workbook = ExcelManager.Default.createExcelWorkbook("사용자 목록", users)
        response.setExcelDownload("user_list(${LocalDate.now()}).xlsx", workbook)
    }
}
