package com.project.youthmoa.api.common.response

import com.project.youthmoa.common.exception.ErrorType
import com.project.youthmoa.common.exception.FieldValidationError

open class ErrorResponse(
    open val errorType: ErrorType,
    open val message: String,
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

class FieldErrorResponse(
    override val errorType: ErrorType,
    override val message: String,
    val fieldErrors: List<FieldValidationError>,
) : ErrorResponse(errorType, message)
