package com.project.youthmoa.domain.model

import com.project.youthmoa.common.exception.ForbiddenException
import com.project.youthmoa.domain.model.converter.CommaToLongListConverter
import com.project.youthmoa.domain.type.Gender
import com.project.youthmoa.domain.type.UserRole
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class User(
    val email: String,
    var phone: String,
    var encPassword: String,
    var name: String,
    var address: String,
    var addressDetail: String,
    var birthday: LocalDate,
    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER,
    @Enumerated(EnumType.STRING)
    var gender: Gender,
    @OneToMany(mappedBy = "applier", cascade = [CascadeType.ALL], orphanRemoval = true)
    val applications: List<ProgramApplication> = emptyList(),
    var lastLoginedAt: LocalDateTime = LocalDateTime.now(),
    @Convert(converter = CommaToLongListConverter::class)
    val managedYouthCenterIds: List<Long> = emptyList(),
) : BaseEntity() {
    fun checkIsAdmin() {
        if (role != UserRole.ADMIN) {
            throw ForbiddenException("관리자만 접근 가능합니다.")
        }
    }
}
