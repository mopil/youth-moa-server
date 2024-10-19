package com.project.youthmoa.common.exception

class InternalServerException(
    message: String = ErrorType.INTERNAL_SERVER_ERROR.defaultMessage,
) : RuntimeException(message) {
    companion object {
        fun ofFileActionFailed(): InternalServerException {
            return InternalServerException(ErrorType.FILE_ACTION_FAILED.defaultMessage)
        }

        fun ofSendEmailFailed(): InternalServerException {
            return InternalServerException(ErrorType.SEND_EMAIL_FAIL.defaultMessage)
        }
    }
}
