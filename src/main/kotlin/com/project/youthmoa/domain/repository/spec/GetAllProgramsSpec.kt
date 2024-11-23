package com.project.youthmoa.domain.repository.spec

import com.project.youthmoa.api.controller.program.request.GetAllProgramsRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

enum class GetAllProgramsSortType {
    LATEST,
    POPULAR,
}

data class GetAllProgramsSpec(
    val youthCenterNames: List<String>? = null,
    val regions: List<String>? = null,
    val filterOnlyActive: Boolean = true,
    val sortType: GetAllProgramsSortType = GetAllProgramsSortType.LATEST,
    val pageable: Pageable,
) {
    companion object {
        fun from(request: GetAllProgramsRequest) =
            GetAllProgramsSpec(
                youthCenterNames = request.youthCenterNames,
                regions = request.regions,
                filterOnlyActive = request.filterOnlyActive,
                sortType = request.sortType,
                pageable = PageRequest.of(request.page, request.size),
            )
    }
}
