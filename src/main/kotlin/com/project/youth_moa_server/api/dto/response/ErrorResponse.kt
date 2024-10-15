package com.project.youth_moa_server.api.dto.response

import com.project.youth_moa_server.common.exception.ErrorType

data class ErrorResponse(
    val errorType: ErrorType,
    val message: String,
) {
    companion object {
        fun from(errorType: ErrorType) = ErrorResponse(
            errorType = errorType,
            message = errorType.defaultMessage
        )
    }
}