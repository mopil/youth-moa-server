package com.project.youthmoa.common.auth

import com.project.youthmoa.common.exception.ForbiddenException
import com.project.youthmoa.common.exception.UnauthorizedException
import com.project.youthmoa.domain.model.User
import org.springframework.security.core.context.SecurityContextHolder

object AuthenticationUtils {
    fun getCurrentLoginUser(): User {
        try {
            val authentication = SecurityContextHolder.getContext().authentication
            val principal = authentication.principal as UserPrincipal
            return principal.user
        } catch (e: Exception) {
            throw UnauthorizedException()
        }
    }

    /**
     * 자기자신에 대한 요청인지 체크
     */
    fun checkIsSelf(userId: Long) {
        val loginUser = getCurrentLoginUser()

        if (loginUser.id != userId) {
            throw ForbiddenException()
        }
    }
}
