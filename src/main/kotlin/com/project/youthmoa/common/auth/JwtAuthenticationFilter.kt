package com.project.youthmoa.common.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.project.youthmoa.api.controller.util.response.ErrorResponse
import com.project.youthmoa.common.auth.AuthenticationUtils.permittedUris
import com.project.youthmoa.common.exception.ErrorType
import com.project.youthmoa.common.util.TokenManager
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val tokenManager: TokenManager,
) : OncePerRequestFilter() {
    private val pathMatcher = AntPathMatcher()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val requestURI = request.requestURI
        val isPermitted =
            permittedUris.any { pattern ->
                pathMatcher.match(pattern, requestURI)
            }

        if (isPermitted) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val token = request.getHeader("Authorization").split(" ").last()
            val authentication = tokenManager.resolveToken(token)
            SecurityContextHolder.getContext().authentication = authentication

            filterChain.doFilter(request, response)
        } catch (ex: Exception) {
            response.respondUnauthorized(ex)
        }
    }

    private fun HttpServletResponse.respondUnauthorized(ex: Exception) {
        val errorType =
            when (ex) {
                is NullPointerException -> {
                    ErrorType.UNAUTHORIZED
                }
                is ExpiredJwtException, is IllegalArgumentException -> {
                    ErrorType.EXPIRED_TOKEN
                }
                else -> {
                    logger.error("JWT resolve error", ex)
                    ErrorType.INVALID_TOKEN
                }
            }

        this.apply {
            status = HttpStatus.UNAUTHORIZED.value()
            contentType = MediaType.APPLICATION_JSON_VALUE
            characterEncoding = Charsets.UTF_8.name()
        }
        ObjectMapper().writeValue(
            this.writer,
            ErrorResponse.from(errorType),
        )
    }
}
