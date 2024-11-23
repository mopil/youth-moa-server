package com.project.youthmoa.domain.service

import com.project.youthmoa.api.controller.program.request.CreateOrUpdateProgramRequest
import com.project.youthmoa.domain.model.Program
import com.project.youthmoa.domain.repository.ProgramRepository
import com.project.youthmoa.domain.repository.YouthCenterRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import com.project.youthmoa.domain.type.ProgramStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

interface ProgramService {
    fun createPrograms(request: CreateOrUpdateProgramRequest): List<Long>

    fun updateProgram(
        programId: Long,
        request: CreateOrUpdateProgramRequest,
    ): Long

    fun deleteProgram(programId: Long)

    @Component
    @Transactional
    class Default(
        private val youthCenterRepository: YouthCenterRepository,
        private val programRepository: ProgramRepository,
    ) : ProgramService {
        override fun createPrograms(request: CreateOrUpdateProgramRequest): List<Long> {
            val youthCenters = youthCenterRepository.findAllById(request.youthCenterIds)

            val programs =
                youthCenters.map {
                    programRepository.save(Program.ofNew(request, it))
                }

            return programs.map { it.id }
        }

        override fun updateProgram(
            programId: Long,
            request: CreateOrUpdateProgramRequest,
        ): Long {
            // TODO: Implement updateProgram
            return 0
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
