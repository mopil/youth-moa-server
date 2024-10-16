package com.project.youthmoa.api.dto.response

import com.project.youthmoa.domain.model.User
import java.time.LocalDateTime

data class FindEmailResponse(
    val email: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(user: User) = FindEmailResponse(user.email, user.createdAt)
    }
}

data class FindEmailListResponse(
    val results: List<FindEmailResponse>,
)
