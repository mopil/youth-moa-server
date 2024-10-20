package com.project.youthmoa.api.app.request

import com.project.youthmoa.domain.model.User
import com.project.youthmoa.domain.type.Gender
import com.project.youthmoa.domain.vo.Email
import com.project.youthmoa.domain.vo.Phone
import com.project.youthmoa.domain.vo.RawPassword
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class CreateUserRequest(
    @Schema(description = "ID(email)", example = "mopil1102@gmail.com")
    val email: String,
    @Schema(description = "비밀번호(암호화 없이 raw 입력값 그대로)")
    val password: String,
    val name: String,
    @Schema(description = "휴대폰 번호(숫자만)", example = "01012341234")
    val phone: Phone,
    val address: String,
    val gender: Gender,
    @Schema(description = "생년월일(yyyy-MM-dd)", example = "1998-11-02")
    val birthday: LocalDate,
) {
    /**
     * value class의 inline 특성 때문에 springdoc의 Schema가 작동하지 않음
     * 따라서 init 블록에서 검증 로직을 추가하여 요청 객체 생성 시 검증 로직을 수행하도록 함
     */
    init {
        Email(email)
        RawPassword(password)
    }

    fun toEntity(encPassword: String) =
        User(
            email = email,
            encPassword = encPassword,
            name = name,
            phone = phone.value,
            address = address,
            gender = gender,
            birthday = birthday,
        )
}
