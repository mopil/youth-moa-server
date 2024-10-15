package com.project.youth_moa_server.api.dto.response

data class PageResponse<T>(
    val content: List<T>,
    val totalCount: Long,
    val size: Long,
    val isLast: Boolean
)