package com.project.youthmoa.api.dto.request

import com.project.youthmoa.domain.model.User
import com.project.youthmoa.domain.type.Gender
import com.project.youthmoa.domain.vo.Email
import com.project.youthmoa.domain.vo.Phone
import com.project.youthmoa.domain.vo.RawPassword
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class CreateUserRequest(
    @Schema(description = "ID(email)", example = "mopil1102@gmail.com")
    val email: Email,
    @Schema(description = "비밀번호(암호화 없이 raw 입력값 그대로)")
    val password: RawPassword,
    val name: String,
    @Schema(description = "휴대폰 번호(숫자만)", example = "01012341234")
    val phone: Phone,
    val address: String,
    val gender: Gender,
    @Schema(description = "생년월일(yyyy-MM-dd)", example = "1998-11-02")
    val birthday: LocalDate,
) {
    fun toEntity(encPassword: String) =
        User(
            email = email.value,
            encPassword = encPassword,
            name = name,
            phone = phone.value,
            address = address,
            gender = gender,
            birthday = birthday,
        )
}
