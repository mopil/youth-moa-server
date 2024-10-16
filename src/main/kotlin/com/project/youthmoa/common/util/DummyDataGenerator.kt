package com.project.youthmoa.common.util

import com.project.youthmoa.domain.model.Program
import com.project.youthmoa.domain.model.YouthCenter
import com.project.youthmoa.domain.repository.ProgramRepository
import com.project.youthmoa.domain.repository.YouthCenterRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.random.Random

@Component
class DummyDataGenerator(
    private val programRepository: ProgramRepository,
    private val youthCenterRepository: YouthCenterRepository,
) {
    @PostConstruct
    @Transactional
    fun init() {
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
                lecturesCommaString = "강좌1,강좌2",
                youthCenter = youthCenters.random(),
            ).also {
                programRepository.save(it)
            }
        }
    }
}
