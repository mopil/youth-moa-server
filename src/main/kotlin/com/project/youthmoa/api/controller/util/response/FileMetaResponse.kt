package com.project.youthmoa.api.controller.util.response

import com.project.youthmoa.domain.model.FileMeta
import java.time.LocalDateTime

data class FileMetaResponse(
    val fileId: Long,
    val uploadUserId: Long,
    val originalFileName: String,
    val serverFileName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(fileMeta: FileMeta): FileMetaResponse {
            return FileMetaResponse(
                fileId = fileMeta.id,
                uploadUserId = fileMeta.uploadUserId,
                originalFileName = fileMeta.originalFileName,
                serverFileName = fileMeta.serverFileName,
                createdAt = fileMeta.createdAt,
                updatedAt = fileMeta.updatedAt,
            )
        }
    }
}
