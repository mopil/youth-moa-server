package com.project.youthmoa.api.controller.program.response

data class GetAllProgramsByAdminResponse(
    val countInfo: ProgramCountResponse,
    val programs: List<ProgramInfoResponse>,
)
