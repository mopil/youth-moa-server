package com.project.youthmoa.common.exception

enum class ErrorType(
    val statusCode: Int,
    val defaultMessage: String,
) {
    BAD_REQUEST(400, "잘못된 요청이에요"),
    NOT_FOUND(404, "존재하지 않는 정보에요"),
    UNAUTHORIZED(401, "로그인 해주세요"),
    INVALID_PASSWORD(401, "비밀번호가 일치하지 않습니다"),
    EXPIRED_TOKEN(401, "만료된 인증 토큰이에요"),
    INVALID_TOKEN(401, "유효하지 않는 인증 토큰이에요"),
    FORBIDDEN(403, "해당 요청을 수행할 권한이 없어요"),
    INTERNAL_SERVER_ERROR(500, "알 수 없는 오류가 발생했어요"),
    TOO_LARGE_FILE(413, "파일 크기가 너무 크네요"),
    FILE_ACTION_FAILED(400, "파일 업/다운로드에 실패했어요"),
    SEND_EMAIL_FAIL(400, "이메일 전송에 실패했어요"),
    TOO_MANY_REQUEST(429, "짧은 시간에 너무 많은 요청을 보냈어요. 잠시 후 다시 시도해주세요"),
}
