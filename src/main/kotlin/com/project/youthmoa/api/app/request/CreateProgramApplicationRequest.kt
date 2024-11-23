package com.project.youthmoa.api.app.request

import io.swagger.v3.oas.annotations.media.Schema

data class CreateProgramApplicationRequest(
    val programId: Long,
    val attachmentFileIds: List<Long> = emptyList(),
    val questionAnswers: List<QuestionAnswer> = emptyList(),
)

data class QuestionAnswer(
    @Schema(description = "질문 ID")
    val questionId: Long,
    @Schema(description = "답변 내용")
    val content: String,
)
