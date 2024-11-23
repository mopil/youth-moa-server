package com.project.youthmoa.api.admin

import com.project.youthmoa.api.admin.request.GetAllUsersRequest
import com.project.youthmoa.api.admin.response.GetAllUsersResponse
import com.project.youthmoa.api.common.response.PageResponse
import com.project.youthmoa.common.util.file.ExcelManager
import com.project.youthmoa.common.util.file.ExcelManager.Default.setExcelDownload
import com.project.youthmoa.common.util.file.UserListExcelRow
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.spec.GetAllUsersSpec
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/admin/users")
class AdminUserController(
    private val userRepository: UserRepository,
) {
    @Operation(summary = "관리자 - 사용자 목록 조회")
    @GetMapping
    fun getAllUsers(request: GetAllUsersRequest): PageResponse<GetAllUsersResponse> {
        return userRepository.findAllBySpec(GetAllUsersSpec.from(request))
            .let { PageResponse.ofGetAllUsers(it) }
    }

    @Operation(summary = "관리자 - 사용자 목록 엑셀 다운로드")
    @GetMapping("/download/excel")
    fun downloadUserExcel(response: HttpServletResponse) {
        val users =
            userRepository.findAll().map {
                UserListExcelRow.from(it)
            }
        val workbook = ExcelManager.Default.createExcelWorkbook("사용자 목록", users)
        response.setExcelDownload("user_list(${LocalDate.now()}).xlsx", workbook)
    }
}
