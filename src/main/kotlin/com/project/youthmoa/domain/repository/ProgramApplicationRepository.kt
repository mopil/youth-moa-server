package com.project.youthmoa.domain.repository

import com.project.youthmoa.domain.model.ProgramApplication
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull

interface ProgramApplicationRepository : JpaRepository<ProgramApplication, Long> {
    fun findByProgramIdAndApplierId(
        programId: Long,
        applierId: Long,
    ): ProgramApplication?

    fun findAllByApplierId(applierId: Long): List<ProgramApplication>

    fun findAllByProgramId(
        programId: Long,
        pageable: Pageable,
    ): Page<ProgramApplication>

    @Query("select p from ProgramApplication p join fetch p.applier where p.program.id = :programId")
    fun findAllByProgramId(programId: Long): List<ProgramApplication>
}

fun ProgramApplicationRepository.findByIdOrThrow(id: Long): ProgramApplication {
    return findByIdOrNull(id) ?: throw NoSuchElementException()
}
