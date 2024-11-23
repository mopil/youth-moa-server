package com.project.youthmoa.common.util.file

import com.project.youthmoa.domain.model.User

interface ExcelRow

data class UserListExcelRow(
    @ExcelColumn(order = 0, name = "이메일")
    val email: String,
    @ExcelColumn(order = 1, name = "휴대폰번호")
    val phone: String,
    @ExcelColumn(order = 2, name = "이름")
    val name: String,
    @ExcelColumn(order = 3, name = "성별")
    val gender: String,
    @ExcelColumn(order = 4, name = "주소")
    val address: String,
    @ExcelColumn(order = 5, name = "생년월일")
    val birthday: String,
    @ExcelColumn(order = 6, name = "최근 로그인 일시")
    val lastLoginedAt: String,
    @ExcelColumn(order = 7, name = "가입일시")
    val createdAt: String,
) : ExcelRow {
    companion object {
        fun from(user: User): UserListExcelRow {
            return UserListExcelRow(
                email = user.email,
                phone = user.phone,
                name = user.name,
                gender = user.gender.name,
                address = user.address,
                birthday = user.birthday.toString(),
                lastLoginedAt = user.lastLoginedAt.toString(),
                createdAt = user.createdAt.toString(),
            )
        }
    }
}
