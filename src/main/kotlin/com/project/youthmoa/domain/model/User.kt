package com.project.youthmoa.domain.model

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
    var birthday: LocalDate,
    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER,
    @Enumerated(EnumType.STRING)
    var gender: Gender,
    @OneToMany(mappedBy = "applier", cascade = [CascadeType.ALL], orphanRemoval = true)
    val applications: List<ProgramApplication> = emptyList(),
    var lastLoginedAt: LocalDateTime = LocalDateTime.now(),
) : BaseEntity()
