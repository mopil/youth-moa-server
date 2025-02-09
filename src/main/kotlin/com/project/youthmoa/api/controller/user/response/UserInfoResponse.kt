package com.project.youthmoa.api.controller.user.response

import com.project.youthmoa.domain.model.User
import com.project.youthmoa.domain.type.Gender
import com.project.youthmoa.domain.type.UserRole
import java.time.LocalDate
import java.time.LocalDateTime

data class UserInfoResponse(
    val id: Long,
    val email: String,
    val role: UserRole,
    val name: String,
    val phone: String,
    val address: String,
    val addressDetail: String,
    val gender: Gender,
    val birthday: LocalDate,
    val lastLoginedAt: LocalDateTime,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(user: User) =
            UserInfoResponse(
                id = user.id,
                email = user.email,
                role = user.role,
                name = user.name,
                phone = user.phone,
                address = user.address,
                addressDetail = user.addressDetail,
                gender = user.gender,
                birthday = user.birthday,
                lastLoginedAt = user.lastLoginedAt,
                createdAt = user.createdAt,
            )
    }
}
