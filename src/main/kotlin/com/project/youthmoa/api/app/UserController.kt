package com.project.youthmoa.api.app

import com.project.youthmoa.api.app.spec.UserApiSpec
import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.api.dto.request.CreateUserRequest
import com.project.youthmoa.api.dto.request.FindEmailRequest
import com.project.youthmoa.api.dto.request.UpdateUserInfoRequest
import com.project.youthmoa.api.dto.request.UserLoginRequest
import com.project.youthmoa.api.dto.response.*
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
) : UserApiSpec {
    @PostMapping("/sign-up")
    override fun signUp(
        @RequestBody request: CreateUserRequest,
    ): UserLoginResponse {
        return userService.signUp(request)
    }

    @PostMapping("/login")
    override fun login(
        @RequestBody request: UserLoginRequest,
    ): UserLoginResponse {
        return userService.login(request)
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

    @AuthenticationRequired
    @PutMapping("/{userId}")
    override fun updateUserInfo(
        @PathVariable userId: Long,
        @RequestBody request: UpdateUserInfoRequest,
    ): UserResponse {
        return userService.updateUserInfo(userId, request)
    }

    @AuthenticationRequired
    @DeleteMapping("/{userId}")
    override fun withdraw(
        @PathVariable userId: Long,
    ) {
        userService.withdraw(userId)
    }
}
