package com.project.youthmoa.api.dto.response

import com.project.youthmoa.domain.model.Program
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

data class PageResponse<T>(
    @Schema(description = "총 요소 개수 (spec 적용)")
    val totalCount: Long,
    @Schema(description = "총 페이지 개수")
    val totalPageCount: Int,
    @Schema(description = "현재 페이지의 요소 개수")
    val size: Int,
    @Schema(description = "마지막 페이지 여부")
    val isLast: Boolean,
    val content: List<T>,
) {
    companion object {
        fun ofGetAllPrograms(page: Page<Program?>): PageResponse<ProgramSimpleResponse> {
            val content =
                page.content.mapNotNull { program ->
                    program?.let { ProgramSimpleResponse.from(it) }
                }
            return PageResponse(
                totalCount = page.totalElements,
                size = content.size,
                isLast = page.isLast,
                totalPageCount = page.totalPages,
                content = content,
            )
        }
    }
}
