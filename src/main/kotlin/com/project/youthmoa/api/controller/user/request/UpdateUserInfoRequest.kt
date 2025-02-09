package com.project.youthmoa.api.controller.user.request

import com.project.youthmoa.domain.type.Gender
import jakarta.validation.constraints.Pattern
import java.time.LocalDate

data class UpdateUserInfoRequest(
    @field:Pattern(regexp = ".*[0-9].*", message = "비밀번호는 숫자를 포함해야 합니다.")
    @field:Pattern(regexp = ".*[a-zA-Z].*", message = "비밀번호는 영문자를 포함해야 합니다.")
    @field:Pattern(regexp = ".{8,20}", message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @field:Pattern(regexp = ".*[!@#$%^&*()].*", message = "비밀번호는 특수문자를 포함해야 합니다.")
    val newPassword: String,
    val newName: String,
    @field:Pattern(regexp = ".*[0-9].*", message = "휴대폰 번호는 숫자만 입력해야 합니다.")
    val newPhone: String,
    val newAddress: String,
    val newAddressDetail: String,
    val newGender: Gender,
    val newBirthday: LocalDate,
)
