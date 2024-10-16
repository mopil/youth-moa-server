package com.project.youthmoa.common.exception

enum class ErrorType(
    val statusCode: Int,
    val defaultMessage: String,
) {
    BAD_REQUEST(400, "잘못된 요청이에요"),
    NOT_FOUND(404, "존재하지 않는 정보에요"),
    UNAUTHORIZED(401, "로그인 해주세요"),
    INVALID_PASSWORD(401, "비밀번호가 일치하지 않습니다"),
    FORBIDDEN(403, "해당 요청을 수행할 권한이 없어요"),
    INTERNAL_SERVER_ERROR(500, "알 수 없는 오류가 발생했어요"),
}
