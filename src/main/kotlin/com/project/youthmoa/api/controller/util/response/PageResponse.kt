package com.project.youthmoa.api.controller.util.response

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

data class PageResponse<T>(
    @Schema(description = "총 요소 개수 (spec 적용)")
    val totalCount: Long,
    @Schema(description = "총 페이지 개수")
    val totalPageCount: Int,
    @Schema(description = "마지막 페이지 여부")
    val isLast: Boolean,
    val content: List<T>,
) {
    @get:Schema(description = "현재 페이지의 요소 개수")
    val size: Int
        get() = content.size

    companion object {
        fun <T> of(
            page: Page<*>,
            content: List<T>,
        ): PageResponse<T> {
            return PageResponse(
                totalCount = page.totalElements,
                totalPageCount = page.totalPages,
                isLast = page.isLast,
                content = content,
            )
        }

        fun <T> from(page: Page<T>): PageResponse<T> {
            return PageResponse(
                totalCount = page.totalElements,
                totalPageCount = page.totalPages,
                isLast = page.isLast,
                content = page.content,
            )
        }
    }
}
