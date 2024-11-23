package com.project.youthmoa.api.controller.user

import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.api.controller.user.request.*
import com.project.youthmoa.api.controller.user.response.FindEmailListResponse
import com.project.youthmoa.api.controller.user.response.FindEmailResponse
import com.project.youthmoa.api.controller.user.response.UserEmailDuplicationCheckResponse
import com.project.youthmoa.api.controller.user.response.UserInfoResponse
import com.project.youthmoa.api.controller.user.response.UserLoginResponse
import com.project.youthmoa.api.controller.util.response.PageResponse
import com.project.youthmoa.common.util.AuthManager
import com.project.youthmoa.common.util.file.ExcelManager
import com.project.youthmoa.common.util.file.ExcelManager.Default.setExcelDownload
import com.project.youthmoa.common.util.file.UserListExcelRow
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.spec.GetAllUsersSpec
import com.project.youthmoa.domain.service.UserInfoService
import com.project.youthmoa.domain.service.UserLoginService
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
class UserController(
    private val userLoginService: UserLoginService,
    private val userInfoService: UserInfoService,
    private val userRepository: UserRepository,
    private val authManager: AuthManager,
) : UserApiDescription {
    @PostMapping("/api/users/sign-up")
    override fun signUp(
        @Valid @RequestBody request: CreateUserRequest,
    ): UserLoginResponse {
        return userLoginService.signUp(request)
    }

    @PostMapping(
        "/api/users/login",
        "/admin/users/login",
    )
    override fun login(
        @Valid @RequestBody request: UserLoginRequest,
    ): UserLoginResponse {
        return userLoginService.login(request)
    }

    @GetMapping("/api/users/email-duplication")
    override fun checkEmailDuplication(
        @RequestParam email: String,
    ): UserEmailDuplicationCheckResponse {
        return if (userRepository.findByEmail(email) == null) {
            UserEmailDuplicationCheckResponse(false)
        } else {
            UserEmailDuplicationCheckResponse(true)
        }
    }

    @GetMapping("/api/users/emails")
    override fun findAllEmailByNameAndPhone(request: FindEmailRequest) =
        userRepository.findAllByNameAndPhone(request.name, request.phone)
            .map { FindEmailResponse.from(it) }
            .let { FindEmailListResponse(it) }

    @PostMapping(
        "/api/users/reset-password",
        "/admin/users/reset-password",
    )
    override fun resetPassword(
        @Valid @RequestBody request: ResetPasswordRequest,
    ) {
        userInfoService.resetPassword(request.email)
    }

    @AuthenticationRequired
    @PutMapping(
        "/api/users/{userId}",
        "/admin/users/{userId}",
    )
    override fun updateUserInfo(
        @PathVariable userId: Long,
        @Valid @RequestBody request: UpdateUserInfoRequest,
    ): UserInfoResponse {
        authManager.checkIsSelf(userId)
        return userInfoService.updateUserInfo(userId, request)
    }

    @AuthenticationRequired
    @DeleteMapping("/api/users/{userId}")
    override fun withdraw(
        @PathVariable userId: Long,
    ) {
        authManager.checkIsSelf(userId)
        userInfoService.withdraw(userId)
    }

    @Operation(summary = "관리자 - 사용자 목록 조회")
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

    @Operation(summary = "관리자 - 사용자 목록 엑셀 다운로드")
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
