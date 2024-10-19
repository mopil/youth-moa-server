package com.project.youthmoa.domain.repository

import com.project.youthmoa.domain.model.ProgramApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface ProgramApplicationRepository : JpaRepository<ProgramApplication, Long> {
    fun findByProgramIdAndApplierId(
        programId: Long,
        applierId: Long,
    ): ProgramApplication?

    fun findAllByApplierId(applierId: Long): List<ProgramApplication>
}

fun ProgramApplicationRepository.findByIdOrThrow(id: Long): ProgramApplication {
    return findByIdOrNull(id) ?: throw NoSuchElementException()
}
