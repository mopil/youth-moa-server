package com.project.youthmoa.domain.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

enum class ApplicationStatus {
    대기,
    반려,
    취소,
    승인,
}

@Entity
class ProgramApplication(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    // 프로그램 신청 시 신청자 정보를 변경해서 제출 할 수 있어서 별도로 관리
    val applierName: String,
    val applierPhone: String,
    val applierGender: Gender,
    val applierBirthday: LocalDate,
    val applierAddress: String,
    @Enumerated(EnumType.STRING)
    var status: ApplicationStatus = ApplicationStatus.대기,
    // 담당자(관리자)가 승인, 반려시 들어가는 정보
    val adminActionDateTime: LocalDateTime? = null,
    var adminComment: String? = null,
    // 신청자가 취소 했을 때 들어가는 정보
    var cancelDateTime: LocalDateTime? = null,
    var cancelReason: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    var program: Program,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var requestUser: User,
) : BaseEntity()
