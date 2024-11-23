package com.project.youthmoa.api.controller.util

import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.api.controller.util.response.FileMetaResponse
import com.project.youthmoa.common.util.AuthManager
import com.project.youthmoa.common.util.RateLimiter
import com.project.youthmoa.common.util.file.FileManager
import com.project.youthmoa.common.util.rateLimit
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "파일")
@RestController
@RequestMapping("/common/files")
class FileController(
    private val fileManager: FileManager,
    @Qualifier("fileUploadRateLimiter") private val rateLimiter: RateLimiter,
    private val authManager: AuthManager,
) : FileApiSpec {
    @AuthenticationRequired
    @PostMapping(
        "/upload",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    override fun uploadFile(
        @RequestPart file: MultipartFile,
    ): FileMetaResponse {
        val loginUser = authManager.getCurrentLoginUser()
        return rateLimit(rateLimiter, loginUser.id) {
            fileManager.uploadFile(loginUser.id, file)
        }
    }

    @GetMapping("/{fileId}/download")
    override fun downloadFile(
        @PathVariable fileId: Long,
    ): ResponseEntity<InputStreamResource> {
        val resource = fileManager.downloadFile(fileId)

        return ResponseEntity.ok()
            .contentType(resource.contentType)
            .contentLength(resource.contentLength)
            .body(resource.inputStreamResource)
    }
}
