package com.project.youthmoa.api.controller.application.response

import com.project.youthmoa.api.controller.program.response.ProgramQuestionResponse
import com.project.youthmoa.domain.model.ProgramFreeQuestion

data class QuestionAnswerResponse(
    val question: ProgramQuestionResponse,
    val answer: String,
) {
    companion object {
        fun of(
            question: ProgramFreeQuestion,
            answer: String,
        ): QuestionAnswerResponse {
            return QuestionAnswerResponse(
                question = ProgramQuestionResponse.of(question),
                answer = answer,
            )
        }
    }
}
