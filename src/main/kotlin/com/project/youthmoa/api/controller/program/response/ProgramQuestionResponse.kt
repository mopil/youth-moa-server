package com.project.youthmoa.api.controller.program.response

import com.project.youthmoa.domain.model.ProgramFreeQuestion

data class ProgramQuestionResponse(
    val questionId: Long,
    val question: String,
) {
    companion object {
        fun of(question: ProgramFreeQuestion): ProgramQuestionResponse {
            return ProgramQuestionResponse(
                questionId = question.id,
                question = question.question,
            )
        }
    }
}
