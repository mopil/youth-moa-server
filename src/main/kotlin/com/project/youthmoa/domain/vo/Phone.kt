package com.project.youthmoa.domain.vo

@JvmInline
value class Phone(
    val value: String,
) {
    init {
        require(value.matches(Regex("^[0-9]*\$"))) {
            "휴대폰 번호는 숫자만 입력 가능합니다."
        }
    }
}
