package com.project.youthmoa.domain.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class YouthCenter(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String,
    val region: String,
    @OneToMany(mappedBy = "youthCenter", cascade = [CascadeType.ALL], orphanRemoval = true)
    val programs: List<Program> = emptyList(),
) : BaseEntity()
