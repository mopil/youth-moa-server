package com.project.youthmoa.common.util

import com.project.youthmoa.api.controller.application.request.ApplyApplicationRequest
import com.project.youthmoa.api.controller.application.request.QuestionAnswer
import com.project.youthmoa.domain.model.*
import com.project.youthmoa.domain.repository.ProgramRepository
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.YouthCenterRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import com.project.youthmoa.domain.service.ProgramApplicationWriteService
import com.project.youthmoa.domain.type.Gender
import com.project.youthmoa.domain.type.ProgramStatus
import com.project.youthmoa.domain.type.UserRole
import jakarta.annotation.PostConstruct
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

const val SUPER_ADMIN_USER_ID = 1L

@Component
class DummyDataGenerator(
    private val userRepository: UserRepository,
    private val programRepository: ProgramRepository,
    private val youthCenterRepository: YouthCenterRepository,
    private val passwordEncoder: PasswordEncoder,
    private val programApplicationWriteService: ProgramApplicationWriteService,
) {
    @PostConstruct
    @Transactional
    fun init() {
//        generateDummyUsers()
//        generateDummyPrograms()

        if (userRepository.count() < 1) {
            generateDummyUsers()
        }

        if (programRepository.count() < 1) {
            generateDummyPrograms()
        }
    }

    private fun generateDummyUsers() {
        val emails =
            listOf(
                "mopil1102@gmail.com",
                "hello2025@naver.com",
                "youthmoa2025@gmail.com",
                "tester1102@naver.com",
                "eodrmfl1004@gmail.com",
            )
        val phones = listOf("01012345678", "01023456789", "01034567890", "01045678901")
        val names = listOf("홍길동", "김철수", "배성흥", "박영수", "이민호", "김민정", "이지은", "박지현", "전예진", "김시현")
        val addresses = listOf("서울시 강남구", "서울시 마포구", "서울시 서대문구", "서울시 종로구")
        User(
            email = "eodrmfl1004@gmail.com",
            phone = phones.random(),
            encPassword = passwordEncoder.encode("1234"),
            name = "전예진",
            address = addresses.random(),
            birthday = LocalDate.of(1998, 12, 12),
            role = UserRole.ADMIN,
            gender = Gender.여,
        ).also {
            userRepository.save(it)
        }
        User(
            email = "mopil1102@gmail.com",
            phone = phones.random(),
            encPassword = passwordEncoder.encode("1234"),
            name = "배성흥",
            address = addresses.random(),
            birthday = LocalDate.of(1998, 11, 2),
            role = UserRole.ADMIN,
            gender = Gender.남,
        ).also {
            userRepository.save(it)
        }
        User(
            email = "park985321@gmail.com",
            phone = phones.random(),
            encPassword = passwordEncoder.encode("1234"),
            name = "김시현",
            address = addresses.random(),
            birthday = LocalDate.of(1998, 1, 1),
            role = UserRole.ADMIN,
            gender = Gender.여,
        ).also {
            userRepository.save(it)
        }
        repeat(100) {
            User(
                email = emails.random() + "$it",
                phone = phones.random(),
                encPassword = passwordEncoder.encode("1234"),
                name = names.random(),
                address = addresses.random(),
                birthday = LocalDate.from(LocalDateTime.now().minusYears(Random.nextLong(20, 30))),
                role = UserRole.entries.toTypedArray().random(),
                gender = Gender.entries.toTypedArray().random(),
            ).also {
                userRepository.save(it)
            }
        }
    }

    private fun generateDummyPrograms() {
        val alphabets = listOf("A", "B", "C", "D", "E")
        val regions = listOf("서울", "경기", "자양동", "마포구")

        val youthCenters = mutableListOf<YouthCenter>()

        repeat(10) {
            YouthCenter(
                name = "청년센터 ${alphabets.random()} $it",
                region = regions.random(),
            ).also {
                youthCenterRepository.save(it).also {
                    youthCenters.add(it)
                }
            }
        }

        repeat(100) {
            Program(
                title = "프로그램 ${alphabets.random()} $it",
                description = "설명 ${alphabets.random()} $it",
                location = "장소 ${alphabets.random()} $it",
                currentAppliedUserCount = Random.nextInt(0, 19),
                maxUserCount = Random.nextInt(20, 50),
                applyStartAt = LocalDateTime.now().minusDays(Random.nextLong(0, 14)),
                applyEndAt = LocalDateTime.now().plusDays(Random.nextLong(0, 7)),
                programStartAt = LocalDateTime.now().minusDays(Random.nextLong(0, 14)),
                programEndAt = LocalDateTime.now().plusDays(Random.nextLong(0, 7)),
                lectures = listOf("강좌1", "강좌2"),
                youthCenter = youthCenters.random(),
                status = ProgramStatus.entries.toTypedArray().random(),
                adminUserId = SUPER_ADMIN_USER_ID,
            ).also {
                programRepository.save(it)
            }
        }

        programRepository.findByIdOrThrow(1).apply {
            freeQuestions =
                mutableListOf(
                    ProgramFreeQuestion(
                        question = "질문1",
                        program = this,
                    ),
                    ProgramFreeQuestion(
                        question = "질문2",
                        program = this,
                    ),
                )
        }.also {
            programRepository.save(it)
        }

        val request =
            ApplyApplicationRequest(
                programId = 1,
                questionAnswers =
                    listOf(
                        QuestionAnswer(
                            questionId = 1,
                            content = "답변1",
                        ),
                        QuestionAnswer(
                            questionId = 2,
                            content = "답변2",
                        ),
                    ),
            )
        programApplicationWriteService.applyApplication(
            SUPER_ADMIN_USER_ID,
            request.programId,
            request.attachmentFileIds,
            request.questionAnswers,
        )

        val request2 =
            ApplyApplicationRequest(
                programId = 2,
            )
        programApplicationWriteService.applyApplication(
            SUPER_ADMIN_USER_ID,
            request2.programId,
            request2.attachmentFileIds,
            request2.questionAnswers,
        )
    }
}
