package com.project.youthmoa.api.controller.common.response

import java.time.Duration
import java.time.LocalDateTime

data class TokenResponse(
    val accessToken: String,
    val expiredAt: LocalDateTime,
    val validHours: Long,
) {
    companion object {
        fun of(
            token: String,
            expiredAt: LocalDateTime,
        ): TokenResponse {
            val now = LocalDateTime.now()
            return TokenResponse(
                accessToken = token,
                expiredAt = expiredAt,
                validHours = Duration.between(now, expiredAt).toHours(),
            )
        }
    }
}
