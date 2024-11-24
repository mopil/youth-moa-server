package com.project.youthmoa.domain.model

import com.project.youthmoa.api.controller.program.request.CreateOrUpdateProgramRequest
import com.project.youthmoa.domain.model.converter.CommaToStringListConverter
import com.project.youthmoa.domain.type.ProgramStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Program(
    var title: String,
    var description: String? = null,
    var location: String? = null,
    var detailContent: String? = null,
    var currentAppliedUserCount: Int = 0,
    var maxUserCount: Int,
    var applyStartAt: LocalDateTime,
    var applyEndAt: LocalDateTime,
    var programStartAt: LocalDateTime,
    var programEndAt: LocalDateTime,
    @Enumerated(EnumType.STRING)
    var status: ProgramStatus,
    @Convert(converter = CommaToStringListConverter::class)
    var attachmentFileIds: List<Long> = emptyList(),
    var programImageFileId: Long? = null,
    var contact: String? = null,
    @Column(name = "lectures")
    @Convert(converter = CommaToStringListConverter::class)
    var lectures: List<String>,
    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL], orphanRemoval = true)
    var freeQuestions: MutableList<ProgramFreeQuestion> = arrayListOf(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    var youthCenter: YouthCenter,
    @OneToMany(mappedBy = "program", cascade = [CascadeType.ALL], orphanRemoval = true)
    var applications: MutableList<ProgramApplication> = arrayListOf(),
) : BaseEntity() {
    fun isEnd() = status == ProgramStatus.마감

    fun isInProgress() = status == ProgramStatus.진행중

    fun addApplication(application: ProgramApplication) {
        applications.add(application)
        currentAppliedUserCount += 1
    }

    fun updateProgram(request: CreateOrUpdateProgramRequest) {
        title = request.title
        description = request.description
        location = request.location
        detailContent = request.detailContent
        maxUserCount = request.maxUserCount
        applyStartAt = request.applyStartAt
        applyEndAt = request.applyEndAt
        programStartAt = request.programStartAt
        programEndAt = request.programEndAt
        attachmentFileIds = request.attachmentFileIds
        programImageFileId = request.programImageFileId
        contact = request.contact
        lectures = request.lectures
        freeQuestions.clear()
        request.freeQuestions.map {
            ProgramFreeQuestion(question = it, program = this)
        }.let(freeQuestions::addAll)
    }

    companion object {
        fun ofNew(
            request: CreateOrUpdateProgramRequest,
            youthCenter: YouthCenter,
        ): Program {
            val status = if (LocalDateTime.now() < request.applyStartAt) ProgramStatus.진행예정 else ProgramStatus.진행중
            val program =
                Program(
                    title = request.title,
                    description = request.description,
                    location = request.location,
                    detailContent = request.detailContent,
                    maxUserCount = request.maxUserCount,
                    applyStartAt = request.applyStartAt,
                    applyEndAt = request.applyEndAt,
                    programStartAt = request.programStartAt,
                    programEndAt = request.programEndAt,
                    attachmentFileIds = request.attachmentFileIds,
                    programImageFileId = request.programImageFileId,
                    contact = request.contact,
                    lectures = request.lectures,
                    status = status,
                    youthCenter = youthCenter,
                )
            request.freeQuestions.map {
                ProgramFreeQuestion(question = it, program = program)
            }.let(program.freeQuestions::addAll)
            return program
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
