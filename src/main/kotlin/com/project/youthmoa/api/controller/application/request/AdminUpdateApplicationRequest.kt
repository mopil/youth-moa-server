package com.project.youthmoa.api.controller.application.request

import com.project.youthmoa.domain.type.ProgramApplicationAdminChangeableStatus

data class AdminUpdateApplicationRequest(
    val adminComment: String?,
    val applicationStatus: ProgramApplicationAdminChangeableStatus?,
)
