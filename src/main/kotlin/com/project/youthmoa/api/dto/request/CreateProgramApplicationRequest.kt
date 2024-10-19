package com.project.youthmoa.api.dto.request

import com.project.youthmoa.domain.vo.Email
import com.project.youthmoa.domain.vo.Phone

data class CreateProgramApplicationRequest(
    val programId: Long,
    val applierName: String,
    val applierPhone: Phone,
    val applierEmail: Email,
    val applierAddress: String,
    val attachmentFileIds: List<Long>,
)
