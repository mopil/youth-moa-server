package com.project.youthmoa.api.controller.admin

import com.project.youthmoa.api.dto.response.GetUserApplicationHistory
import com.project.youthmoa.api.dto.response.ProgramDetailResponse
import com.project.youthmoa.api.dto.response.UserResponse
import com.project.youthmoa.domain.repository.ProgramApplicationRepository
import com.project.youthmoa.domain.repository.ProgramRepository
import com.project.youthmoa.domain.repository.UserRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "어드민")
@RestController
@RequestMapping("/admin")
class AdminController(
    private val programRepository: ProgramRepository,
    private val userRepository: UserRepository,
    private val programApplicationRepository: ProgramApplicationRepository,
) {
    @GetMapping("/programs")
    fun getAllPrograms() = programRepository.findAll().map { ProgramDetailResponse.from(it) }

    @GetMapping("/users")
    fun getAllUsers() = userRepository.findAll().map { UserResponse.from(it) }

    @GetMapping("/applications")
    fun getAllApplications() = programApplicationRepository.findAll().map { GetUserApplicationHistory.from(it) }
}
