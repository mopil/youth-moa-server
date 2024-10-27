package com.project.youthmoa.domain.repository

import com.project.youthmoa.domain.model.FileMeta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface FileMetaRepository : JpaRepository<FileMeta, Long>

fun FileMetaRepository.findByIdOrThrow(id: Long): FileMeta {
    return findByIdOrNull(id) ?: throw NoSuchElementException("$id 에 해당하는 파일 정보가 없습니다.")
}
