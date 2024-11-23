package com.project.youthmoa.api.controller.common

import com.project.youthmoa.api.controller.common.response.GetAllYouthCentersResponse
import com.project.youthmoa.api.controller.common.response.YouthCenterResponse
import com.project.youthmoa.domain.repository.YouthCenterRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "청년센터")
@RestController
@RequestMapping("/common/youth-centers")
class YouthCenterController(
    private val youthCenterRepository: YouthCenterRepository,
) {
    @Operation(summary = "청년센터 목록 조회")
    @GetMapping
    fun getAllYouthCenters(): GetAllYouthCentersResponse {
        return youthCenterRepository.findAll().map {
            YouthCenterResponse.from(it)
        }.let { GetAllYouthCentersResponse(it) }
    }
}
