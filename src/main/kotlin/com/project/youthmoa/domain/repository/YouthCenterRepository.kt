package com.project.youthmoa.domain.repository

import com.project.youthmoa.domain.model.YouthCenter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface YouthCenterRepository : JpaRepository<YouthCenter, Long>

fun YouthCenterRepository.findByIdOrThrow(id: Long): YouthCenter {
    return findByIdOrNull(id) ?: throw NoSuchElementException()
}
