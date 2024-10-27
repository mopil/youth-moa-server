package com.project.youthmoa.common.exception

import org.springframework.validation.FieldError

data class FieldValidationError(
    val fieldName: String,
    val value: Any,
    val reason: String,
) {
    companion object {
        fun from(fieldError: FieldError) =
            FieldValidationError(
                fieldName = fieldError.field,
                value = fieldError.rejectedValue!!,
                reason = fieldError.defaultMessage ?: "유효하지 않은 값입니다.",
            )
    }
}
