package com.project.youthmoa.api.controller.user.response

import com.project.youthmoa.domain.model.User
import com.project.youthmoa.domain.type.Gender
import java.time.LocalDate
import java.time.LocalDateTime

data class UserInfoResponse(
    val id: Long,
    val name: String,
    val phone: String,
    val address: String,
    val gender: Gender,
    val birthday: LocalDate,
    val lastLoginedAt: LocalDateTime,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(user: User) =
            UserInfoResponse(
                id = user.id,
                name = user.name,
                phone = user.phone,
                address = user.address,
                gender = user.gender,
                birthday = user.birthday,
                lastLoginedAt = user.lastLoginedAt,
                createdAt = user.createdAt,
            )
    }
}
