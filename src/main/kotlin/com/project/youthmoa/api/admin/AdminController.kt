package com.project.youthmoa.api.admin

import com.project.youthmoa.api.app.response.GetUserApplicationHistory
import com.project.youthmoa.api.app.response.ProgramDetailResponse
import com.project.youthmoa.domain.repository.ProgramApplicationRepository
import com.project.youthmoa.domain.repository.ProgramRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "어드민")
@RestController
@RequestMapping("/admin")
class AdminController(
    private val programRepository: ProgramRepository,
    private val programApplicationRepository: ProgramApplicationRepository,
) {
    @GetMapping("/programs")
    fun getAllPrograms() = programRepository.findAll().map { ProgramDetailResponse.from(it) }

    @GetMapping("/applications")
    fun getAllApplications() = programApplicationRepository.findAll().map { GetUserApplicationHistory.from(it) }
}
