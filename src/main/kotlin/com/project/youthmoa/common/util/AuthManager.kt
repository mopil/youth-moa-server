package com.project.youthmoa.common.util

import com.project.youthmoa.common.auth.UserPrincipal
import com.project.youthmoa.common.exception.ForbiddenException
import com.project.youthmoa.common.exception.UnauthorizedException
import com.project.youthmoa.domain.model.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

interface AuthManager {
    /**
     * 현재 로그인한(JWT 인증 된) 사용자 정보 반환
     */
    fun getCurrentLoginUser(): User

    /**
     * 현재 로그인한 관리자 정보 반환
     */
    fun getCurrentLoginAdmin(): User {
        val loginUser = getCurrentLoginUser()
        loginUser.checkIsAdmin()
        return loginUser
    }

    /**
     * 현재 로그인한 사용자가 userId와 일치하는지 확인
     */
    fun checkIsSelf(userId: Long)

    @Component
    class Default : AuthManager {
        override fun getCurrentLoginUser(): User {
            try {
                val authentication = SecurityContextHolder.getContext().authentication
                val principal = authentication.principal as UserPrincipal
                return principal.user
            } catch (e: Exception) {
                throw UnauthorizedException()
            }
        }

        override fun checkIsSelf(userId: Long) {
            val loginUser = getCurrentLoginUser()

            if (loginUser.id != userId) {
                throw ForbiddenException()
            }
        }
    }
}
