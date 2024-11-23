package com.project.youthmoa.api.controller.util.response

import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType

data class DownloadFileResource(
    val contentType: MediaType,
    val contentLength: Long,
    val inputStreamResource: InputStreamResource,
)
