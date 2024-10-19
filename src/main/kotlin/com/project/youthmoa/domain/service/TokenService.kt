package com.project.youthmoa.domain.service

import com.project.youthmoa.api.dto.response.TokenResponse
import com.project.youthmoa.common.auth.UserPrincipal
import com.project.youthmoa.common.util.Logger.logger
import com.project.youthmoa.common.util.SUPER_ADMIN_USER_ID
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.security.Key
import java.time.ZonedDateTime
import java.util.*

interface TokenService {
    fun generateAccessToken(
        userId: Long,
        userRole: String,
    ): TokenResponse

    fun resolveToken(token: String): Authentication

    @Service
    class Default(
        @Value("\${jwt.secret-key}") private val secretKey: String,
        @Value("\${jwt.ttl-seconds}") private val tokenValidSeconds: Long,
        private val userRepository: UserRepository,
    ) : TokenService {
        val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray())

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
            val token =
                Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(Date.from(now.toInstant()))
                    .setExpiration(Date.from(expiredAt.toInstant()))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact()
            return TokenResponse.of(token, expiredAt.toLocalDateTime())
        }

        override fun resolveToken(token: String): Authentication {
            // 0000은 테스트용 토큰 (어드민 자동 로그인)
            val userId =
                if (token == "0000") {
                    logger.info("Super admin auto login")
                    SUPER_ADMIN_USER_ID
                } else {
                    val claims =
                        Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .body
                    claims["userId"] as Int
                }
            val user = userRepository.findByIdOrThrow(userId.toLong())
            // authorities를 넣어주지 않으면 isAuthentication=false로 설정되니 주의
            return UsernamePasswordAuthenticationToken(
                UserPrincipal(user),
                null,
                emptyList(),
            )
        }
    }
}
