package com.project.youthmoa.domain.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity
class YouthCenter(
    val name: String,
    val region: String,
    @OneToMany(mappedBy = "youthCenter", cascade = [CascadeType.ALL], orphanRemoval = true)
    val programs: List<Program> = emptyList(),
) : BaseEntity()
