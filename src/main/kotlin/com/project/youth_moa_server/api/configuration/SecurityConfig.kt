package com.project.youth_moa_server.api.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/admin/**").hasAnyRole("ADMIN")
                .requestMatchers(*permittedUris).permitAll()
                .anyRequest().authenticated()
            }
        return http.build()
    }
}

val permittedUris = arrayOf(
    "/swagger/**",
    "/swagger-ui/**",
    "/webjars/**",
    "/api/users/email-duplication",
    "/api/users/login",
    "/api/users/sign-up"
)