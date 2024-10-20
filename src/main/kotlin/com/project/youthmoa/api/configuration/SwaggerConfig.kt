package com.project.youthmoa.api.configuration

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

const val SECURITY_SCHEME_NAME = "Bearer #{Access Token}"

@Configuration
@SecurityScheme(
    name = SECURITY_SCHEME_NAME,
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
)
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .components(Components())
            .info(apiInfo())
    }

    private fun apiInfo(): Info {
        return Info()
            .title("Youth-Moa API")
            .description("청년모아 APP REST API")
            .version("1.0.0")
    }

    @Bean
    fun appApiGroup() =
        GroupedOpenApi.builder()
            .group("api")
            .packagesToScan("com.project.youthmoa.api.app")
            .build()

    @Bean
    fun adminApiGroup() =
        GroupedOpenApi.builder()
            .group("admin")
            .packagesToScan("com.project.youthmoa.api.admin")
            .build()

    @Bean
    fun commonApiGroup() =
        GroupedOpenApi.builder()
            .group("common")
            .packagesToScan("com.project.youthmoa.api.common")
            .build()
}

/**
 * - Swagger에서 JWT 토큰 인증이 필요한 API를 표시한다 (자물쇠 모양)
 * - API별로 인증이 필요한지 안한지 표시한다
 */
@SecurityRequirement(name = SECURITY_SCHEME_NAME)
annotation class AuthenticationRequired
