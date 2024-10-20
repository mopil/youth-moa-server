package com.project.youthmoa.api.admin.response

import com.project.youthmoa.domain.model.User
import com.project.youthmoa.domain.type.UserRole
import java.time.LocalDateTime

data class GetAllUsersResponse(
    val userId: Long,
    val name: String,
    val email: String,
    val role: UserRole,
    val phone: String,
    val createdAt: LocalDateTime,
    val lastLoginedAt: LocalDateTime,
    val isDeleted: Boolean,
) {
    companion object {
        fun from(user: User) =
            GetAllUsersResponse(
                userId = user.id,
                name = user.name,
                email = user.email,
                role = user.role,
                phone = user.phone,
                createdAt = user.createdAt,
                lastLoginedAt = user.lastLoginedAt,
                isDeleted = user.isDeleted,
            )
    }
}
