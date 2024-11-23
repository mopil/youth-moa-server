package com.project.youthmoa.api.configuration

import com.project.youthmoa.api.controller.common.response.ErrorResponse
import com.project.youthmoa.api.controller.common.response.FieldErrorResponse
import com.project.youthmoa.common.exception.*
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException

@RestControllerAdvice
class GlobalControllerAdvice(
    @Value("\${spring.servlet.multipart.max-file-size}") private val maxFileSize: String,
) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFoundException(): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.from(ErrorType.NOT_FOUND))
    }

    @ExceptionHandler(
        IllegalStateException::class,
        IllegalArgumentException::class,
        HttpMessageNotReadableException::class,
        MethodArgumentNotValidException::class,
    )
    fun handleBadRequestException(ex: Exception): ResponseEntity<ErrorResponse> {
        if (ex is MethodArgumentNotValidException) {
            val validationErrors =
                ex.bindingResult.fieldErrors
                    .map { FieldValidationError.from(it) }
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                    FieldErrorResponse(
                        errorType = ErrorType.BAD_REQUEST,
                        message = "유효하지 않은 값입니다.",
                        fieldErrors = validationErrors,
                    ),
                )
        } else {
            val message =
                when (ex) {
                    is HttpMessageNotReadableException -> {
                        ex.cause?.cause?.message
                    }
                    else -> {
                        ex.message
                    }
                }
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                    ErrorResponse.withMessageOrDefault(ErrorType.BAD_REQUEST, message),
                )
        }
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<ErrorResponse> {
        logger.warn(ex.message)
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse.withMessageOrDefault(ErrorType.UNAUTHORIZED, ex.message))
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleUnauthorizedException(ex: ForbiddenException): ResponseEntity<ErrorResponse> {
        logger.warn(ex.message)
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse.withMessageOrDefault(ErrorType.FORBIDDEN, ex.message))
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxSizeException(e: MaxUploadSizeExceededException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body(
                ErrorResponse.withMessageOrDefault(
                    ErrorType.TOO_LARGE_FILE,
                    "파일이 너무 커요. 크기는 $maxFileSize 이하여야 해요",
                ),
            )
    }

    @ExceptionHandler(RateLimitExceededException::class)
    fun handleRateLimitException(e: RateLimitExceededException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .body(
                ErrorResponse.from(ErrorType.TOO_MANY_REQUEST),
            )
    }

    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
    )
    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("예상치 못한 에러 발생", ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse.from(ErrorType.INTERNAL_SERVER_ERROR),
            )
    }
}
