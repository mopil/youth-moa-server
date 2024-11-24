package com.project.youthmoa.api.controller.application.response

import com.project.youthmoa.api.controller.program.response.ProgramCountResponse

data class GetAllApplicationsByUserResponse(
    val countInfo: ProgramCountResponse,
    val applications: List<GetProgramApplicationResponse>,
)
