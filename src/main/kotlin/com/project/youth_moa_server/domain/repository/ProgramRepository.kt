package com.project.youth_moa_server.domain.repository

import com.project.youth_moa_server.domain.model.Program
import org.springframework.data.jpa.repository.JpaRepository

interface ProgramRepository : JpaRepository<Program, Long> {
}