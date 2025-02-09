package com.project.youthmoa.api.controller.user.request

import com.project.youthmoa.domain.model.User
import com.project.youthmoa.domain.type.Gender
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import java.time.LocalDate

data class CreateUserRequest(
    @Schema(description = "ID(email)", example = "mopil1102@gmail.com")
    @field:Email
    val email: String,
    @Schema(description = "비밀번호(암호화 없이 raw 입력값 그대로)")
    @field:Pattern(regexp = ".*[0-9].*", message = "비밀번호는 숫자를 포함해야 합니다.")
    @field:Pattern(regexp = ".*[a-zA-Z].*", message = "비밀번호는 영문자를 포함해야 합니다.")
    @field:Pattern(regexp = ".{8,20}", message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @field:Pattern(regexp = ".*[!@#$%^&*()].*", message = "비밀번호는 특수문자를 포함해야 합니다.")
    val password: String,
    val name: String,
    @Schema(description = "휴대폰 번호(숫자만)", example = "01012341234")
    @field:Pattern(regexp = ".*[0-9].*", message = "휴대폰 번호는 숫자만 입력해야 합니다.")
    val phone: String,
    val address: String,
    val addressDetail: String,
    val gender: Gender,
    @Schema(description = "생년월일(yyyy-MM-dd)", example = "1998-11-02")
    val birthday: LocalDate,
) {
    fun toEntity(encPassword: String) =
        User(
            email = email,
            encPassword = encPassword,
            name = name,
            phone = phone,
            address = address,
            addressDetail = addressDetail,
            gender = gender,
            birthday = birthday,
        )
}
