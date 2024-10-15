package com.project.youth_moa_server.api.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
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
    fun appApiGroup() = GroupedOpenApi.builder()
        .group("api")
        .packagesToScan("com.project.youth_moa_server.api.app")
        .build()

    @Bean
    fun adminApiGroup() = GroupedOpenApi.builder()
        .group("admin")
        .packagesToScan("com.project.youth_moa_server.api.admin")
        .build()


}