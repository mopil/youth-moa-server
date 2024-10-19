package com.project.youthmoa.common.exception

class RateLimitExceededException(
    message: String = ErrorType.TOO_MANY_REQUEST.defaultMessage,
) : RuntimeException(message)
