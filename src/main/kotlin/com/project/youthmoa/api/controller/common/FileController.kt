package com.project.youthmoa.api.controller.common

import com.project.youthmoa.api.configuration.AuthenticationRequired
import com.project.youthmoa.api.controller.common.spec.FileApiSpec
import com.project.youthmoa.api.dto.response.FileMetaResponse
import com.project.youthmoa.common.auth.AuthenticationUtils
import com.project.youthmoa.common.util.FileService
import io.swagger.v3.oas.annotations.tags.Tag
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
    private val fileService: FileService,
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
        val loginUser = AuthenticationUtils.getCurrentLoginUser()
        return fileService.uploadFile(loginUser.id, file)
    }

    @GetMapping("/{fileId}/download")
    override fun downloadFile(
        @PathVariable fileId: Long,
    ): ResponseEntity<InputStreamResource> {
        val resource = fileService.downloadFile(fileId)

        return ResponseEntity.ok()
            .contentType(resource.contentType)
            .contentLength(resource.contentLength)
            .body(resource.inputStreamResource)
    }
}
