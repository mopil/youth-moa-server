package com.project.youth_moa_server.api.dto.response

import com.project.youth_moa_server.domain.model.Gender
import com.project.youth_moa_server.domain.model.User
import java.time.LocalDate

data class UserResponse(
    val id: Long,
    val name: String,
    val encPassword: String,
    val phone: String,
    val address: String,
    val gender: Gender,
    val birthday: LocalDate
) {
    companion object {
        fun from(user: User) = UserResponse(
            id = user.id,
            name = user.name,
            encPassword = user.encPassword,
            phone = user.phone,
            address = user.address,
            gender = user.gender,
            birthday = user.birthday
        )
    }
}