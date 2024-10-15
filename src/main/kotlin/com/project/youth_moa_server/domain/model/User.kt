package com.project.youth_moa_server.domain.model

import jakarta.persistence.*
import java.time.LocalDate

enum class Gender {
    남, 여
}

@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val email: String,
    var phone: String,
    var encPassword: String,
    var name: String,
    var address: String,
    var birthday: LocalDate,
    @Enumerated(EnumType.STRING)
    var gender: Gender,

    @OneToMany(mappedBy = "requestUser", cascade = [CascadeType.ALL], orphanRemoval = true)
    val applications: List<ProgramApplication> = emptyList()
    ) : BaseEntity()