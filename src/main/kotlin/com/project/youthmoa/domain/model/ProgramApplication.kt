package com.project.youthmoa.domain.model

import com.project.youthmoa.common.exception.ForbiddenException
import com.project.youthmoa.domain.model.converter.CommaToLongListConverter
import com.project.youthmoa.domain.type.ProgramApplicationStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class ProgramApplication(
    @Enumerated(EnumType.STRING)
    var status: ProgramApplicationStatus = ProgramApplicationStatus.대기,
    // 담당자(관리자)가 승인, 반려시 들어가는 정보
    val adminActionDateTime: LocalDateTime? = null,
    var adminComment: String? = null,
    // 신청자가 취소 했을 때 들어가는 정보
    var cancelDateTime: LocalDateTime? = null,
    var cancelReason: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    val program: Program,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val applier: User,
    @Column(name = "attachment_file_ids")
    @Convert(converter = CommaToLongListConverter::class)
    var attachmentFileIds: List<Long>,
    @OneToMany(mappedBy = "programApplication", cascade = [CascadeType.ALL], orphanRemoval = true)
    val answers: MutableList<ProgramApplicationAnswer> = mutableListOf(),
) : BaseEntity() {
    fun isApproved() = status == ProgramApplicationStatus.승인

    fun isRejected() = status == ProgramApplicationStatus.반려

    fun isWaiting() = status == ProgramApplicationStatus.대기

    fun addAnswers(answers: List<ProgramApplicationAnswer>) {
        this.answers.addAll(answers)
    }

    fun cancelByUser(
        userId: Long,
        cancelReason: String,
    ) {
        if (isApproved()) {
            throw IllegalStateException("승인된 건은 취소할 수 없습니다.")
        }

        if (isRejected()) {
            throw IllegalStateException("반려된 건은 취소할 수 없습니다.")
        }

        if (applier.id != userId) {
            throw ForbiddenException("신청자만 취소할 수 있습니다.")
        }

        status = ProgramApplicationStatus.취소
        cancelDateTime = LocalDateTime.now()
        this.cancelReason = cancelReason
        program.currentAppliedUserCount--
    }
}

@Entity
class ProgramApplicationAnswer(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    val programApplication: ProgramApplication,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    val question: ProgramFreeQuestion,
    val answer: String,
) : BaseEntity()
