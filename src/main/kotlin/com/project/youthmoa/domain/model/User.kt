package com.project.youthmoa.domain.model

import jakarta.persistence.*
import java.time.LocalDate

enum class Gender {
    남,
    여,
}

@Entity
class User(
    val email: String,
    var phone: String,
    var encPassword: String,
    var name: String,
    var address: String,
    var birthday: LocalDate,
    @Enumerated(EnumType.STRING)
    var gender: Gender,
    @OneToMany(mappedBy = "applier", cascade = [CascadeType.ALL], orphanRemoval = true)
    val applications: List<ProgramApplication> = emptyList(),
) : BaseEntity()
