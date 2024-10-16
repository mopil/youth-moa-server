package com.project.youthmoa.api.dto.response

import com.project.youthmoa.common.exception.ErrorType

data class ErrorResponse(
    val errorType: ErrorType,
    val message: String,
) {
    companion object {
        fun from(errorType: ErrorType) =
            ErrorResponse(
                errorType = errorType,
                message = errorType.defaultMessage,
            )

        fun withMessageOrDefault(
            errorType: ErrorType,
            message: String?,
        ) = ErrorResponse(
            errorType = errorType,
            message = message ?: errorType.defaultMessage,
        )
    }
}
