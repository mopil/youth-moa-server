package com.project.youthmoa.common.configuration

import com.project.youthmoa.common.util.RateLimiter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class RateLimiterConfig() {
    @Bean
    fun fileUploadRateLimiter(): RateLimiter {
        return RateLimiter.CaffeineLocalCacheImpl(
            limit = 5,
            duration = 10,
            timeUnit = TimeUnit.SECONDS,
            maxSize = 100,
        )
    }

    @Bean
    fun programApplicationRateLimiter(): RateLimiter {
        return RateLimiter.CaffeineLocalCacheImpl(
            limit = 5,
            duration = 24,
            timeUnit = TimeUnit.HOURS,
            maxSize = 100,
        )
    }
}
