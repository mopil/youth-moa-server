package com.project.youthmoa.api.dto.request

data class CreateProgramApplicationRequest(
    val programId: Long,
    val applierName: String,
    val applierPhone: String,
    val applierEmail: String,
    val applierAddress: String,
    val attachmentUrls: List<String>,
)
