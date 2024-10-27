package com.project.youthmoa.domain.model

import com.project.youthmoa.api.admin.request.CreateProgramRequest
import com.project.youthmoa.domain.model.converter.CommaToStringListConverter
import com.project.youthmoa.domain.type.ProgramStatus
import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Program(
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
    @Convert(converter = CommaToStringListConverter::class)
    var attachmentFileIds: List<Long> = emptyList(),
    var programImageFileId: Long? = null,
    var contactNumber: String? = null,
    // 프로그램 신청 시 관리자 수락이 필요한지 여부
    var isNeedApprove: Boolean = true,
    @Column(name = "lectures")
    @Convert(converter = CommaToStringListConverter::class)
    var lectures: List<String>,
    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL], orphanRemoval = true)
    val freeQuestions: MutableList<ProgramFreeQuestion> = arrayListOf(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    var youthCenter: YouthCenter,
    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL], orphanRemoval = true)
    var applications: MutableList<ProgramApplication> = arrayListOf(),
) : BaseEntity() {
    fun isEnd() = status == ProgramStatus.마감

    fun isInProgress() = status == ProgramStatus.진행중

    fun addAppliedUserCount() {
        val nextUserCount = currentAppliedUserCount + 1
        if (nextUserCount > maxUserCount) {
            throw IllegalStateException("최대 신청 인원($maxUserCount)을 초과하였습니다.")
        }

        currentAppliedUserCount = nextUserCount

        if (nextUserCount == maxUserCount) {
            status = ProgramStatus.마감
        }
    }

    companion object {
        fun ofNew(
            request: CreateProgramRequest,
            youthCenter: YouthCenter,
        ): Program {
            return Program(
                title = request.title,
                description = request.description,
                location = request.location,
                detailContent = request.detailContent,
                maxUserCount = request.maxUserCount,
                applyStartDate = request.applyStartDate,
                applyEndDate = request.applyEndDate,
                programStartDate = request.programStartDate,
                programEndDate = request.programEndDate,
                attachmentFileIds = request.attachmentFileIds,
                programImageFileId = request.programImageFileId,
                contactNumber = request.contactNumber,
                isNeedApprove = request.isNeedApprove,
                lectures = request.lectures,
                youthCenter = youthCenter,
            )
        }
    }
}

@Entity
class ProgramFreeQuestion(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val question: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    val program: Program,
)
