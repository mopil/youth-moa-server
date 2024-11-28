package com.project.youthmoa.common.auth

object AuthenticationUtils {
    val permittedUris =
        arrayOf(
            "/swagger/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/v3/api-docs/**",
            "/api/users/email-duplication",
            "/api/users/login",
            "/api/users/sign-up",
            "/api/users/reset-password",
            "/api/users/emails",
            "/api/programs",
            "/api/programs/{programId}",
            "/admin/{adminUserId}/super-login",
            "/admin/**",
            "/health",
        )
}
