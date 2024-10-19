package com.project.youthmoa.domain.vo

@JvmInline
value class RawPassword(
    val value: String,
) {
    init {
        require(value.isNotEmpty()) { "비밀번호는 비어있을 수 없습니다." }
        require(value.length >= 8) { "비밀번호는 8자 이상이어야 합니다." }
        require(value.length <= 20) { "비밀번호는 20자 이하여야 합니다." }
        require(value.matches(Regex(".*[0-9].*"))) { "비밀번호는 숫자를 포함해야 합니다." }
        require(value.matches(Regex(".*[a-zA-Z].*"))) { "비밀번호는 영문자를 포함해야 합니다." }
        require(value.matches(Regex(".*[!@#\$%^&*].*"))) { "비밀번호는 특수문자를 포함해야 합니다." }
    }
}
