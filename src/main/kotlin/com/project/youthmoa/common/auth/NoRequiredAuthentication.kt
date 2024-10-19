package com.project.youthmoa.common.auth

object NoRequiredAuthentication {
    val permittedUris =
        arrayOf(
            "/swagger/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/api/users/email-duplication",
            "/api/users/login",
            "/api/users/sign-up",
            "/api/users/emails",
            "/api/programs",
            "/api/programs/{programId}",
        )
}
