package com.project.youthmoa.api.app

import com.project.youthmoa.api.app.request.*
import com.project.youthmoa.api.app.response.*
import com.project.youthmoa.api.app.spec.UserApiSpec
import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.common.util.AuthManager
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.service.UserInfoService
import com.project.youthmoa.domain.service.UserLoginService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userLoginService: UserLoginService,
    private val userInfoService: UserInfoService,
    private val userRepository: UserRepository,
    private val authManager: AuthManager,
) : UserApiSpec {
    @PostMapping("/sign-up")
    override fun signUp(
        @Valid @RequestBody request: CreateUserRequest,
    ): UserLoginResponse {
        return userLoginService.signUp(request)
    }

    @PostMapping("/login")
    override fun login(
        @Valid @RequestBody request: UserLoginRequest,
    ): UserLoginResponse {
        return userLoginService.login(request)
    }

    @GetMapping("/email-duplication")
    override fun checkEmailDuplication(
        @RequestParam email: String,
    ): UserEmailDuplicationCheckResponse {
        return if (userRepository.findByEmail(email) == null) {
            UserEmailDuplicationCheckResponse(false)
        } else {
            UserEmailDuplicationCheckResponse(true)
        }
    }

    @GetMapping("/emails")
    override fun findAllEmailByNameAndPhone(request: FindEmailRequest) =
        userRepository.findAllByNameAndPhone(request.name, request.phone)
            .map { FindEmailResponse.from(it) }
            .let { FindEmailListResponse(it) }

    @PostMapping("/reset-password")
    override fun resetPassword(
        @Valid @RequestBody request: ResetPasswordRequest,
    ) {
        userInfoService.resetPassword(request.email)
    }

    @AuthenticationRequired
    @PutMapping("/{userId}")
    override fun updateUserInfo(
        @PathVariable userId: Long,
        @Valid @RequestBody request: UpdateUserInfoRequest,
    ): UserResponse {
        authManager.checkIsSelf(userId)
        return userInfoService.updateUserInfo(userId, request)
    }

    @AuthenticationRequired
    @DeleteMapping("/{userId}")
    override fun withdraw(
        @PathVariable userId: Long,
    ) {
        authManager.checkIsSelf(userId)
        userInfoService.withdraw(userId)
    }
}
