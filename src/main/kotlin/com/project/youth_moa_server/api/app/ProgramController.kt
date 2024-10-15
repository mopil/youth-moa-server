package com.project.youth_moa_server.api.app

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "프로그램")
@RestController
@RequestMapping("/api/programs")
class ProgramController {

    @Operation(summary = "프로그램 목록 조회")
    @GetMapping
    fun getAllPrograms() {

    }

    @Operation(summary = "진행중인, 종료된 프로그램 수 조회")
    @DeleteMapping("/count")
    fun getProgramsCount() {

    }

    @Operation(summary = "프로그램 참가 신청")
    @PostMapping("/{programId}/applications")
    fun createApplication() {

    }

    @Operation(summary = "프로그램 신청 취소")
    @DeleteMapping("/{programId}/applications/{applicationId}")
    fun cancelApplication() {

    }
}