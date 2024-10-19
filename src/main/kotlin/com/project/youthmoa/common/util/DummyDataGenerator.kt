package com.project.youthmoa.common.util

import com.project.youthmoa.domain.model.*
import com.project.youthmoa.domain.repository.ProgramRepository
import com.project.youthmoa.domain.repository.UserRepository
import com.project.youthmoa.domain.repository.YouthCenterRepository
import com.project.youthmoa.domain.type.Gender
import com.project.youthmoa.domain.type.UserRole
import jakarta.annotation.PostConstruct
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.random.Random

const val SUPER_ADMIN_USER_ID = 1L

@Component
class DummyDataGenerator(
    private val userRepository: UserRepository,
    private val programRepository: ProgramRepository,
    private val youthCenterRepository: YouthCenterRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @PostConstruct
    @Transactional
    fun init() {
        userRepository.save(
            User(
                email = "mopil1102@gmail.com",
                phone = "01012345678",
                encPassword = passwordEncoder.encode("1234"),
                name = "홍길동",
                address = "서울시 강남구",
                birthday = LocalDate.of(1998, 11, 2),
                role = UserRole.ADMIN,
                gender = Gender.남,
            ),
        )

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
                applyStartDate = LocalDate.now().minusDays(Random.nextLong(0, 14)),
                applyEndDate = LocalDate.now().plusDays(Random.nextLong(0, 7)),
                programStartDate = LocalDate.now().minusDays(Random.nextLong(0, 14)),
                programEndDate = LocalDate.now().plusDays(Random.nextLong(0, 7)),
                lectures = listOf("강좌1", "강좌2"),
                youthCenter = youthCenters.random(),
            ).also {
                programRepository.save(it)
            }
        }
    }
}
