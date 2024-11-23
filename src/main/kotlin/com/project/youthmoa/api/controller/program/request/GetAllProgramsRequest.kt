package com.project.youthmoa.api.controller.program.request

import com.project.youthmoa.domain.repository.spec.GetAllProgramsSortType
import io.swagger.v3.oas.annotations.Parameter

data class GetAllProgramsRequest(
    @field:Parameter(description = "필터: 청년센터 이름 (복수 가능) default=null", required = false)
    val youthCenterNames: List<String>? = null,
    @field:Parameter(description = "필터: 지역명 (복수 가능) default=null", required = false)
    val regions: List<String>? = null,
    @field:Parameter(description = "필터: 활성화된 프로그램만 보기 여부 (default=true)", required = true, example = "true")
    val filterOnlyActive: Boolean = true,
    @field:Parameter(description = "정렬 기준 (default=LATEST)", required = true)
    val sortType: GetAllProgramsSortType = GetAllProgramsSortType.LATEST,
    @field:Parameter(description = "페이지 번호 (default=0)", required = true, example = "0")
    val page: Int = 0,
    @field:Parameter(description = "페이지 크기 (default=9)", required = true, example = "9")
    val size: Int = 9,
)
