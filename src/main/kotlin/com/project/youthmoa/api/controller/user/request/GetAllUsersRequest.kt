package com.project.youthmoa.api.controller.user.request

import com.project.youthmoa.domain.type.Gender
import com.project.youthmoa.domain.type.UserRole
import io.swagger.v3.oas.annotations.Parameter
import org.springdoc.core.annotations.ParameterObject

@ParameterObject
data class GetAllUsersRequest(
    @field:Parameter(description = "권한 (default:null)", example = "null", required = false)
    val roles: List<UserRole>? = null,
    @field:Parameter(description = "성별 (default:null)", example = "null", required = false)
    val gender: Gender? = null,
    @field:Parameter(description = "비활성화 여부 (default:null)", example = "null", required = false)
    val isDeleted: Boolean? = null,
    @field:Parameter(description = "검색어 (이름, 이메일, 휴대폰번호 like 검색)", required = false)
    val searchKeyword: String? = null,
    @field:Parameter(description = "페이지 (default:0)", example = "0", required = true)
    val page: Int = 0,
    @field:Parameter(description = "사이즈 (default:9)", example = "9", required = true)
    val size: Int = 9,
)
