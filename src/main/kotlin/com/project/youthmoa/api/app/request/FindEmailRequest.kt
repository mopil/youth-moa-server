package com.project.youthmoa.api.app.request

import com.project.youthmoa.domain.vo.Phone
import io.swagger.v3.oas.annotations.Parameter

data class FindEmailRequest(
    @field:Parameter(description = "유저 이름", required = true)
    val name: String,
    @field:Parameter(description = "휴대폰 번호 (오직 숫자만)", required = true)
    val phone: String,
) {
    init {
        Phone(phone)
    }
}
