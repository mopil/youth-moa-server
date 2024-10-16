package com.project.youthmoa.api.app

import com.project.youthmoa.api.configuration.LoginRequired
import com.project.youthmoa.api.dto.request.CreateUserRequest
import com.project.youthmoa.api.dto.request.FindEmailRequest
import com.project.youthmoa.api.dto.request.UpdateUserInfoRequest
import com.project.youthmoa.api.dto.request.UserLoginRequest
import com.project.youthmoa.api.dto.response.*
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
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

    @Operation(summary = "이름, 휴대폰 번호로 아이디(이메일) 찾기")
    @GetMapping("/emails")
    fun findAllEmailByNameAndPhone(
        @ParameterObject request: FindEmailRequest,
    ) = userRepository.findAllByNameAndPhone(request.name, request.phone)
        .map { FindEmailResponse.from(it) }
        .let { FindEmailListResponse(it) }

    @Operation(summary = "사용자 정보 수정")
    @LoginRequired
    @PutMapping("/{userId}")
    fun updateUserInfo(
        @PathVariable userId: Long,
        @RequestBody request: UpdateUserInfoRequest,
    ): UserResponse {
        return userService.updateUserInfo(userId, request)
    }

    @Operation(summary = "회원탈퇴")
    @LoginRequired
    @DeleteMapping("/{userId}")
    fun withdraw(
        @PathVariable userId: Long,
    ) {
        userService.withdraw(userId)
    }
}
