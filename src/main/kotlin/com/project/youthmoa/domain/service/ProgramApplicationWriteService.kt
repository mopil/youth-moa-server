package com.project.youthmoa.domain.service

import com.project.youthmoa.api.controller.application.request.QuestionAnswer
import com.project.youthmoa.domain.model.Program
import com.project.youthmoa.domain.model.ProgramApplication
import com.project.youthmoa.domain.model.ProgramApplicationAnswer
import com.project.youthmoa.domain.repository.*
import com.project.youthmoa.domain.type.ProgramApplicationAdminChangeableStatus
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ProgramApplicationWriteService {
    fun applyApplication(
        userId: Long,
        programId: Long,
        attachmentFileIds: List<Long> = emptyList(),
        questionAnswers: List<QuestionAnswer> = emptyList(),
    ): Long

    fun updateApplicationByAdmin(
        applicationId: Long,
        adminUserId: Long,
        adminComment: String?,
        applicationStatus: ProgramApplicationAdminChangeableStatus?,
    )

    fun cancelApplicationByUser(
        userId: Long,
        applicationId: Long,
        cancelReason: String,
    )
}

@Service
class ProgramApplicationWriteServiceImpl(
    private val userRepository: UserRepository,
    private val programRepository: ProgramRepository,
    private val programApplicationRepository: ProgramApplicationRepository,
) : ProgramApplicationWriteService {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 사용자 - 프로그램 신청
     */
    @Transactional
    override fun applyApplication(
        userId: Long,
        programId: Long,
        attachmentFileIds: List<Long>,
        questionAnswers: List<QuestionAnswer>,
    ): Long {
        val program = programRepository.findFetchWithQuestionsOrThrow(programId)
        logger.info("신청서 작성 시작 = programId:${program.id}, userId:$userId")

        checkBeforeApply(program.id, userId)

        val user = userRepository.findByIdOrThrow(userId)

        val application =
            ProgramApplication(
                program = program,
                attachmentFileIds = attachmentFileIds,
                status = ProgramApplicationStatus.대기,
                applier = user,
            ).also {
                programApplicationRepository.save(it)
            }

        if (program.freeQuestions.isNotEmpty()) {
            addAnswers(questionAnswers, program, application)
        }

        program.addApplication(application)

        return application.id
    }

    /**
     * 관리자 - 프로그램 신청서 상태 변경 (승인/반려, 관리자 코멘트)
     */
    @Transactional
    override fun updateApplicationByAdmin(
        applicationId: Long,
        adminUserId: Long,
        adminComment: String?,
        applicationStatus: ProgramApplicationAdminChangeableStatus?,
    ) {
        val application = programApplicationRepository.findByIdOrThrow(applicationId)
        val status =
            when (applicationStatus) {
                ProgramApplicationAdminChangeableStatus.승인 -> ProgramApplicationStatus.승인
                ProgramApplicationAdminChangeableStatus.반려 -> ProgramApplicationStatus.반려
                null -> null
            }
        adminComment?.let { application.adminComment = it }
        status?.let { application.status = it }
    }

    /**
     * 사용자 - 프로그램 신청서 취소
     */
    @Transactional
    override fun cancelApplicationByUser(
        userId: Long,
        applicationId: Long,
        cancelReason: String,
    ) {
        val application: ProgramApplication = programApplicationRepository.findByIdOrThrow(applicationId)
        application.cancelByUser(userId, cancelReason)
    }

    private fun checkBeforeApply(
        programId: Long,
        userId: Long,
    ) {
        programApplicationRepository.findByProgramIdAndApplierId(programId, userId)?.let {
            if (it.isApproved()) {
                throw IllegalStateException("이미 승인 완료 되었습니다.")
            }
            if (it.isWaiting()) {
                throw IllegalStateException("이미 신청하여 승인 대기중입니다.")
            }
        }
        logger.info("이전 신청서 상태 체크 완료 (이상없음)")
    }

    private fun addAnswers(
        userAnswers: List<QuestionAnswer>,
        program: Program,
        application: ProgramApplication,
    ) {
        val userAnswerQuestionIds = userAnswers.map { it.questionId }
        if (userAnswerQuestionIds.any { it !in program.freeQuestions.map { it.id } }) {
            throw IllegalArgumentException("질문이 존재하지 않습니다.")
        }

        val questionMap = program.freeQuestions.associateBy { it.id }
        val answers =
            userAnswers.map {
                ProgramApplicationAnswer(
                    question = questionMap[it.questionId] ?: throw IllegalArgumentException("질문이 존재하지 않습니다."),
                    answer = it.content,
                    programApplication = application,
                )
            }
        application.addAnswers(answers)
        logger.info("질문 존재하여 답변 추가 = $userAnswers")
    }
}
