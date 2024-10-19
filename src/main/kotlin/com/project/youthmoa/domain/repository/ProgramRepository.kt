package com.project.youthmoa.domain.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.project.youthmoa.domain.model.Program
import com.project.youthmoa.domain.model.YouthCenter
import com.project.youthmoa.domain.repository.spec.GetAllProgramsSortType
import com.project.youthmoa.domain.repository.spec.GetAllProgramsSpec
import com.project.youthmoa.domain.type.ProgramStatus
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

interface ProgramRepository : JpaRepository<Program, Long>, ProgramJdslRepository

fun ProgramRepository.findByIdOrThrow(id: Long): Program {
    return findByIdOrNull(id) ?: throw NoSuchElementException()
}

interface ProgramJdslRepository {
    fun findAllBySpec(spec: GetAllProgramsSpec): Page<Program?>
}

@Repository
class ProgramJdslRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
) : ProgramJdslRepository {
    override fun findAllBySpec(spec: GetAllProgramsSpec): Page<Program?> {
        return kotlinJdslJpqlExecutor.findPage(spec.pageable) {
            val order =
                if (spec.sortType == GetAllProgramsSortType.POPULAR) {
                    path(Program::currentAppliedUserCount).desc()
                } else {
                    path(Program::updatedAt).desc()
                }
            select(entity(Program::class))
                .from(
                    entity(Program::class),
                    join(Program::youthCenter),
                )
                .whereAnd(
                    if (spec.filterOnlyActive) path(Program::status).eq(ProgramStatus.진행중) else null,
                    spec.regions?.let {
                        path(YouthCenter::region).`in`(it)
                    },
                    spec.youthCenterNames?.let {
                        path(YouthCenter::name).`in`(it)
                    },
                    path(Program::isDeleted).eq(false),
                )
                .orderBy(order)
        }
    }
}
