package com.project.youthmoa.domain.model

import jakarta.persistence.Entity

@Entity
class FileMeta(
    val originalFileName: String,
    val serverFileName: String,
    val url: String,
    val uploadUserId: Long,
) : BaseEntity()
