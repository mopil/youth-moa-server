package com.project.youthmoa.api.app

import com.project.youthmoa.api.app.request.GetAllProgramsRequest
import com.project.youthmoa.api.app.response.PageResponse
import com.project.youthmoa.api.app.response.ProgramDetailResponse
import com.project.youthmoa.api.app.response.ProgramSimpleResponse
import com.project.youthmoa.api.app.spec.ProgramApiSpec
import com.project.youthmoa.domain.repository.ProgramRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.repository.spec.GetAllProgramsSpec
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/programs")
class ProgramController(
    private val programRepository: ProgramRepository,
) : ProgramApiSpec {
    @GetMapping
    override fun getAllPrograms(request: GetAllProgramsRequest): PageResponse<ProgramSimpleResponse> {
        return programRepository.findAllBySpec(GetAllProgramsSpec.from(request))
            .let { PageResponse.ofGetAllPrograms(it) }
    }

    @GetMapping("/{programId}")
    override fun getProgramDetailInfo(
        @PathVariable programId: Long,
    ): ProgramDetailResponse {
        return programRepository.findByIdOrThrow(programId)
            .let { ProgramDetailResponse.from(it) }
    }
}
