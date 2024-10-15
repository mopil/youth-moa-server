package com.project.youthmoa.domain.repository

import com.project.youthmoa.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}

fun UserRepository.findByEmailOrThrow(email: String): User = findByEmail(email) ?: throw NoSuchElementException()
