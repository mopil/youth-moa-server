package com.project.youthmoa.domain.repository

import com.linecorp.kotlinjdsl.querymodel.jpql.entity.Entities.entity
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.project.youthmoa.domain.model.Program
import com.project.youthmoa.domain.model.ProgramApplication
import com.project.youthmoa.domain.model.User
import com.project.youthmoa.domain.repository.dto.ProgramApplicationWithAppliedCount
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

interface ProgramApplicationRepository :
    JpaRepository<ProgramApplication, Long>,
    ProgramApplicationJdslRepository {
    fun findByProgramIdAndApplierId(
        programId: Long,
        applierId: Long,
    ): ProgramApplication?

    fun findAllByApplierId(applierId: Long): List<ProgramApplication>

    @Query("select p from ProgramApplication p join fetch p.applier where p.program.id = :programId")
    fun findAllByProgramId(programId: Long): List<ProgramApplication>
}

fun ProgramApplicationRepository.findByIdOrThrow(id: Long): ProgramApplication {
    return findByIdOrNull(id) ?: throw NoSuchElementException()
}

interface ProgramApplicationJdslRepository {
    fun findAllByProgramIdAndAppliedCount(
        programId: Long,
        pageable: Pageable,
    ): Page<ProgramApplicationWithAppliedCount?>
}

@Repository
class ProgramApplicationJdslRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
) : ProgramApplicationJdslRepository {
    /**
     * 프로그램에 신청한 신청서 정보들을 조회한다.
     * - appliedCount: 해당 사용자가 전체 프로그램에 신청한 횟수 (해당 프로그램에 국한되지 않고 전체임)
     * - application: programId에 해당하는 프로그램 신청서 정보
     * - applied_application: 해당 사용자의 전체 프로그램 신청 횟수 카운트를 위한 정보
     */
    override fun findAllByProgramIdAndAppliedCount(
        programId: Long,
        pageable: Pageable,
    ): Page<ProgramApplicationWithAppliedCount?> {
        val application = entity(ProgramApplication::class, "application")
        val appliedApplication = entity(ProgramApplication::class, "applied_application")
        return kotlinJdslJpqlExecutor.findPage(pageable) {
            selectNew<ProgramApplicationWithAppliedCount>(
                application.path(ProgramApplication::applier),
                application.path(ProgramApplication::createdAt),
                application.path(ProgramApplication::status),
                count(appliedApplication),
            )
                .from(
                    application,
                    join(application.path(ProgramApplication::applier)),
                    join(appliedApplication).on(
                        application.path(ProgramApplication::applier).path(User::id)
                            .eq(appliedApplication.path(ProgramApplication::applier).path(User::id)),
                    ),
                )
                .where(
                    application.path(ProgramApplication::program).path(Program::id).eq(programId),
                ).groupBy(
                    application.path(ProgramApplication::applier),
                    application.path(ProgramApplication::createdAt),
                    application.path(ProgramApplication::status),
                )
        }
    }
}
