package com.project.youthmoa.domain.repository.dto

import com.project.youthmoa.domain.model.User
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import java.time.LocalDateTime

data class ProgramApplicationWithAppliedCount(
    val applier: User,
    val appliedAt: LocalDateTime,
    val applicationStatus: ProgramApplicationStatus,
    val appliedCount: Long,
)
