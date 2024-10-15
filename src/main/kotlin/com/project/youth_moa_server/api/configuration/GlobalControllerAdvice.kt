package com.project.youth_moa_server.api.configuration

import com.project.youth_moa_server.api.dto.response.ErrorResponse
import com.project.youth_moa_server.common.exception.ErrorType
import com.project.youth_moa_server.common.exception.UnauthorizedException
import com.project.youth_moa_server.common.util.Logger.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFoundException(): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.from(ErrorType.NOT_FOUND))
    }

    @ExceptionHandler(
        IllegalStateException::class,
        IllegalArgumentException::class
    )
    fun handleBadRequestException(ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    errorType = ErrorType.BAD_REQUEST,
                    message = ex.message ?: ErrorType.BAD_REQUEST.defaultMessage
                )
            )
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse.from(ErrorType.UNAUTHORIZED))
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("예상치 못한 에러 발생 = ${ex.message}")
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse.from(ErrorType.INTERNAL_SERVER_ERROR)
            )
    }
}