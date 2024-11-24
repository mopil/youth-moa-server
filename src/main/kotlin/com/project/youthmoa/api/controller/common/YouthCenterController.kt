package com.project.youthmoa.api.controller.common

import com.project.youthmoa.api.controller.common.response.GetAllYouthCentersResponse
import com.project.youthmoa.api.controller.common.response.YouthCenterResponse
import com.project.youthmoa.domain.model.YouthCenter
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.YouthCenterRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "청년센터")
@RestController
@RequestMapping("/common/youth-centers")
class YouthCenterController(
    private val userRepository: UserRepository,
    private val youthCenterRepository: YouthCenterRepository,
) {
    @Operation(summary = "청년센터 목록 조회")
    @GetMapping
    fun getAllYouthCenters(): GetAllYouthCentersResponse {
        return youthCenterRepository.findAll().toResponse()
    }

    @Operation(summary = "해당 어드민 소속의 청년센터 목록 조회")
    @GetMapping("/of-admin")
    fun getAllYouthCentersOfAdmin(
        @RequestParam adminUserId: Long,
    ): GetAllYouthCentersResponse {
        val admin = userRepository.findByIdOrThrow(adminUserId)
        return youthCenterRepository.findAllById(admin.managedYouthCenterIds)
            .toResponse()
    }

    private fun List<YouthCenter>.toResponse(): GetAllYouthCentersResponse {
        return GetAllYouthCentersResponse(
            this.map { YouthCenterResponse.from(it) },
        )
    }
}
