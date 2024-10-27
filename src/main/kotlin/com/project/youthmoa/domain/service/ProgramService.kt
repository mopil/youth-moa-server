package com.project.youthmoa.domain.service

import com.project.youthmoa.api.admin.request.CreateProgramRequest
import com.project.youthmoa.domain.model.Program
import com.project.youthmoa.domain.repository.ProgramRepository
import com.project.youthmoa.domain.repository.YouthCenterRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import com.project.youthmoa.domain.type.ProgramStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

interface ProgramService {
    fun createProgram(request: CreateProgramRequest): Long

    fun updateProgram(
        programId: Long,
        request: CreateProgramRequest,
    ): Long

    fun deleteProgram(programId: Long)

    @Component
    @Transactional
    class Default(
        private val youthCenterRepository: YouthCenterRepository,
        private val programRepository: ProgramRepository,
    ) : ProgramService {
        override fun createProgram(request: CreateProgramRequest): Long {
            val youthCenter = youthCenterRepository.findByIdOrThrow(request.youthCenterId)

            val program = programRepository.save(Program.ofNew(request, youthCenter))

            return program.id
        }

        override fun updateProgram(
            programId: Long,
            request: CreateProgramRequest,
        ): Long {
            TODO("Not yet implemented")
        }

        override fun deleteProgram(programId: Long) {
            val program = programRepository.findByIdOrThrow(programId)
            program.apply {
                isDeleted = true
                status = ProgramStatus.종료
                applications.forEach {
                    it.status = ProgramApplicationStatus.취소
                    it.cancelReason = "프로그램 삭제로 인한 취소"
                }
            }
        }
    }
}
