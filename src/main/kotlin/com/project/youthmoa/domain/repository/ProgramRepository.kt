package com.project.youthmoa.domain.repository

import com.project.youthmoa.domain.model.Program
import org.springframework.data.jpa.repository.JpaRepository

interface ProgramRepository : JpaRepository<Program, Long>
