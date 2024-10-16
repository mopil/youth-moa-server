package com.project.youthmoa.api.dto.request

import com.project.youthmoa.domain.model.Gender
import java.time.LocalDate

data class UpdateUserInfoRequest(
    val newPassword: String,
    val newName: String,
    val newPhone: String,
    val newAddress: String,
    val newGender: Gender,
    val newBirthday: LocalDate,
)
