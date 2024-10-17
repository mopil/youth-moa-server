package com.project.youthmoa.common.exception

class ForbiddenException(
    message: String = ErrorType.FORBIDDEN.defaultMessage,
) : RuntimeException(message)
