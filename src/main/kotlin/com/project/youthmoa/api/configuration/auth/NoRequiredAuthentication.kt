package com.project.youthmoa.api.configuration.auth

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
        )
}
