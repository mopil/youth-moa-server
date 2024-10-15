package com.project.youthmoa.api.app

import com.project.youthmoa.api.configuration.LoginRequired
import com.project.youthmoa.api.dto.request.CreateUserRequest
import com.project.youthmoa.api.dto.request.UserLoginRequest
import com.project.youthmoa.api.dto.response.UserEmailDuplicationCheckResponse
import com.project.youthmoa.api.dto.response.UserLoginResponse
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "회원")
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
) {
    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody request: CreateUserRequest,
    ): UserLoginResponse {
        return userService.signUp(request)
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    fun login(
        @RequestBody request: UserLoginRequest,
    ): UserLoginResponse {
        return userService.login(request)
    }

    @Operation(summary = "아이디(이메일) 중복 확인")
    @GetMapping("/email-duplication")
    fun checkEmailDuplication(
        @RequestParam email: String,
    ): UserEmailDuplicationCheckResponse {
        return if (userRepository.findByEmail(email) == null) {
            UserEmailDuplicationCheckResponse(false)
        } else {
            UserEmailDuplicationCheckResponse(true)
        }
    }

    @Operation(summary = "아이디 찾기")
    @PostMapping("/find-id")
    fun findId() {
        // TODO
    }

    @Operation(summary = "비밀번호 찾기")
    @PostMapping("/find-password")
    fun findPassword() {
        // TODO
    }

    @Operation(summary = "사용자 정보 수정")
    @LoginRequired
    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: Long,
    ) {
        // TODO
    }

    @Operation(summary = "회원탈퇴")
    @LoginRequired
    @DeleteMapping("/{userId}")
    fun deleteUser(
        @PathVariable userId: Long,
    ) {
        // TODO
    }
}
