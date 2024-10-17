package com.project.youthmoa.api.app

import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.api.dto.request.GetAllProgramsRequest
import com.project.youthmoa.api.dto.response.PageResponse
import com.project.youthmoa.api.dto.response.ProgramSimpleResponse
import com.project.youthmoa.api.dto.spec.GetAllProgramsSpec
import com.project.youthmoa.domain.repository.ProgramRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "프로그램")
@RestController
@RequestMapping("/api/programs")
class ProgramController(
    private val programRepository: ProgramRepository,
) {
    @Operation(summary = "프로그램 목록 조회")
    @GetMapping
    fun getAllPrograms(
        @ParameterObject request: GetAllProgramsRequest,
    ): PageResponse<ProgramSimpleResponse> {
        return programRepository.findAllBySpec(GetAllProgramsSpec.from(request))
            .let { PageResponse.ofGetAllPrograms(it) }
    }

    @Operation(summary = "진행중인, 종료된 프로그램 수 조회")
    @GetMapping("/count")
    fun getProgramsCount() {
    }

    @Operation(summary = "프로그램 참가 신청")
    @AuthenticationRequired
    @PostMapping("/{programId}/applications")
    fun createApplication() {
    }

    @Operation(summary = "프로그램 신청 취소")
    @AuthenticationRequired
    @DeleteMapping("/{programId}/applications/{applicationId}")
    fun cancelApplication() {
    }
}
