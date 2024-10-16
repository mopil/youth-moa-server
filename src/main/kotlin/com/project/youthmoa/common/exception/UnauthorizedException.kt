package com.project.youthmoa.common.exception

class UnauthorizedException(
    message: String = ErrorType.UNAUTHORIZED.defaultMessage,
) : RuntimeException(message)
