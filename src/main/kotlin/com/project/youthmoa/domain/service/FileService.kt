package com.project.youthmoa.domain.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.project.youthmoa.api.dto.response.DownloadFileResource
import com.project.youthmoa.api.dto.response.FileMetaResponse
import com.project.youthmoa.domain.model.FileMeta
import com.project.youthmoa.domain.repository.FileMetaRepository
import com.project.youthmoa.domain.repository.findByIdOrThrow
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

interface FileService {
    fun uploadFile(
        userId: Long,
        multipartFile: MultipartFile,
    ): FileMetaResponse

    fun downloadFile(fileId: Long): DownloadFileResource
}

@Service
class FileServiceS3Impl(
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String,
    private val awsS3Client: AmazonS3Client,
    private val fileMetaRepository: FileMetaRepository,
) : FileService {
    @Transactional
    override fun uploadFile(
        userId: Long,
        multipartFile: MultipartFile,
    ): FileMetaResponse {
        val originalFileName = multipartFile.originalFilename ?: throw IllegalArgumentException("파일 이름이 없습니다.")
        val serverFileName = "${userId}_${LocalDateTime.now()}_$originalFileName"
        ObjectMetadata().apply {
            contentType = multipartFile.contentType
            contentLength = multipartFile.size
        }.let {
            awsS3Client.putObject(bucket, serverFileName, multipartFile.inputStream, it)
        }

        val fileMeta =
            FileMeta(
                uploadUserId = userId,
                originalFileName = originalFileName,
                serverFileName = serverFileName,
                url = awsS3Client.getUrl(bucket, serverFileName).toString(),
            ).let { fileMetaRepository.save(it) }

        return FileMetaResponse.from(fileMeta)
    }

    override fun downloadFile(fileId: Long): DownloadFileResource {
        val fileMeta = fileMetaRepository.findByIdOrThrow(fileId)
        val s3Object = awsS3Client.getObject(bucket, fileMeta.serverFileName)
        return DownloadFileResource(
            contentType =
                s3Object.objectMetadata.contentType.let {
                    MediaType.parseMediaType(it)
                },
            contentLength = s3Object.objectMetadata.contentLength,
            inputStreamResource = InputStreamResource(s3Object.objectContent),
        )
    }
}
