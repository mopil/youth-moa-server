package com.project.youthmoa.domain.service

import com.project.youthmoa.api.dto.response.TokenResponse
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*

interface TokenService {
    fun generateAccessToken(
        userId: Long,
        userRole: String,
    ): TokenResponse

    @Service
    class Default(
        @Value("\${jwt.secret-key}") private val secretKey: String,
        @Value("\${jwt.ttl-seconds}") private val tokenValidSeconds: Long,
    ) : TokenService {
        override fun generateAccessToken(
            userId: Long,
            userRole: String,
        ): TokenResponse {
            val claims =
                Jwts.claims().apply {
                    put("userId", userId)
                    put("role", userRole)
                }
            val now = ZonedDateTime.now()
            val expiredAt = now.plusSeconds(tokenValidSeconds)
            val key = Keys.hmacShaKeyFor(secretKey.toByteArray())
            val token =
                Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(Date.from(now.toInstant()))
                    .setExpiration(Date.from(expiredAt.toInstant()))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact()
            return TokenResponse.of(token, expiredAt.toLocalDateTime())
        }
    }
}
