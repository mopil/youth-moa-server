package com.project.youthmoa.api.admin

import com.project.youthmoa.api.admin.request.GetAllUsersRequest
import com.project.youthmoa.api.admin.response.GetAllUsersResponse
import com.project.youthmoa.api.app.response.PageResponse
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.spec.GetAllUsersSpec
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/users")
class AdminUserController(
    private val userRepository: UserRepository,
) {
    @GetMapping
    fun getAllUsers(request: GetAllUsersRequest): PageResponse<GetAllUsersResponse> {
        return userRepository.findAllBySpec(GetAllUsersSpec.from(request))
            .let { PageResponse.ofGetAllUsers(it) }
    }
}
