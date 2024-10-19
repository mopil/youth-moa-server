package com.project.youthmoa.api.dto.request

import com.project.youthmoa.domain.type.Gender
import com.project.youthmoa.domain.vo.Phone
import com.project.youthmoa.domain.vo.RawPassword
import java.time.LocalDate

data class UpdateUserInfoRequest(
    val newPassword: RawPassword,
    val newName: String,
    val newPhone: Phone,
    val newAddress: String,
    val newGender: Gender,
    val newBirthday: LocalDate,
)
