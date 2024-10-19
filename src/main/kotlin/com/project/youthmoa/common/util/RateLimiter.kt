package com.project.youthmoa.common.util

import com.github.benmanes.caffeine.cache.Caffeine
import com.project.youthmoa.common.exception.RateLimitExceededException
import java.util.concurrent.TimeUnit

interface RateLimiter {
    fun checkLimit(userId: Long)

    fun increaseCount(userId: Long)

    class CaffeineLocalCacheImpl(
        val limit: Int,
        val duration: Long,
        val timeUnit: TimeUnit,
        val maxSize: Long = 100,
    ) : RateLimiter {
        private val cache =
            Caffeine
                .newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(duration, timeUnit)
                .build<String, Any>()

        override fun checkLimit(userId: Long) {
            val uploadCount = cache.getIfPresent(userId.toString()) as Int?
            if (uploadCount != null && uploadCount >= limit) {
                throw RateLimitExceededException()
            }
        }

        override fun increaseCount(userId: Long) {
            val uploadCount = cache.getIfPresent(userId.toString()) as Int?
            if (uploadCount == null) {
                cache.put(userId.toString(), 1)
            } else {
                cache.put(userId.toString(), uploadCount + 1)
            }
        }
    }
}

fun <T> rateLimit(
    rateLimiter: RateLimiter,
    userId: Long,
    block: () -> T,
): T {
    rateLimiter.checkLimit(userId)
    val result = block()
    rateLimiter.increaseCount(userId)
    return result
}
