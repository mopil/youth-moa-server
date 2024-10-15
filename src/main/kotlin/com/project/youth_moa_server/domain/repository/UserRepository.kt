package com.project.youth_moa_server.domain.repository

import com.project.youth_moa_server.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}

fun UserRepository.findByEmailOrThrow(email: String): User =
    findByEmail(email) ?: throw NoSuchElementException()