package com.project.youthmoa.api.app.request

import com.project.youthmoa.domain.type.Gender
import com.project.youthmoa.domain.vo.Phone
import com.project.youthmoa.domain.vo.RawPassword
import java.time.LocalDate

data class UpdateUserInfoRequest(
    val newPassword: String,
    val newName: String,
    val newPhone: String,
    val newAddress: String,
    val newGender: Gender,
    val newBirthday: LocalDate,
) {
    init {
        RawPassword(newPassword)
        Phone(newPhone)
    }
}
