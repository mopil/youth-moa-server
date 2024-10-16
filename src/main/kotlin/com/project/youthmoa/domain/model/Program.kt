package com.project.youthmoa.domain.model

import jakarta.persistence.*
import java.time.LocalDate

enum class ProgramStatus {
    진행중,
    마감,
}

@Entity
class Program(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    var title: String,
    var description: String? = null,
    // 진행장소는 freetext인가?
    var location: String? = null,
    var detailContent: String? = null,
    var currentAppliedUserCount: Int = 0,
    var maxUserCount: Int,
    var applyStartDate: LocalDate,
    var applyEndDate: LocalDate,
    var programStartDate: LocalDate,
    var programEndDate: LocalDate,
    @Enumerated(EnumType.STRING)
    var status: ProgramStatus = ProgramStatus.진행중,
    var attachmentUrl: String? = null,
    var programImageUrl: String? = null,
    var contactNumber: String? = null,
    // 프로그램 신청 시 관리자 수락이 필요한지 여부
    var isNeedApprove: Boolean = true,
    @Column(name = "lectures")
    var lecturesCommaString: String,
    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL], orphanRemoval = true)
    val freeQuestions: List<ProgramFreeQuestion> = emptyList(),
    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL], orphanRemoval = true)
    val choiceQuestions: List<ProgramChoiceQuestion> = emptyList(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    var youthCenter: YouthCenter,
) : BaseEntity()

@Entity
class ProgramFreeQuestion(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val question: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    val program: Program,
)

@Entity
class ProgramChoiceQuestion(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    // TODO: 선택지를 어떻게 할지?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    val program: Program,
)
