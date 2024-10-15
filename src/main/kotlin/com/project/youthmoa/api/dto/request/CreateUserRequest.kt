package com.project.youthmoa.api.dto.request

import com.project.youthmoa.domain.model.Gender
import com.project.youthmoa.domain.model.User
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class CreateUserRequest(
    @Schema(description = "ID(email)", example = "mopil1102@gmail.com")
    val email: String,
    @Schema(description = "비밀번호(암호화 없이 raw 입력값 그대로)")
    val password: String,
    val name: String,
    @Schema(description = "휴대폰 번호(숫자만)", example = "01012341234")
    val phone: String,
    val address: String,
    val gender: Gender,
    @Schema(description = "생년월일(yyyy-MM-dd)", example = "1998-11-02")
    val birthday: LocalDate,
) {
    init {
        // TODO: add validations
    }

    fun toEntity(encPassword: String) =
        User(
            email = email,
            encPassword = encPassword,
            name = name,
            phone = phone,
            address = address,
            gender = gender,
            birthday = birthday,
        )
}
