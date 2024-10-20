package com.project.youthmoa.domain.repository.spec

import com.project.youthmoa.api.admin.request.GetAllUsersRequest
import com.project.youthmoa.domain.type.Gender
import com.project.youthmoa.domain.type.UserRole
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

data class GetAllUsersSpec(
    val roles: List<UserRole>? = null,
    val gender: Gender? = null,
    val searchKeyword: String? = null,
    val isDeleted: Boolean? = null,
    val pageable: Pageable,
) {
    companion object {
        fun from(request: GetAllUsersRequest): GetAllUsersSpec {
            return GetAllUsersSpec(
                roles = request.roles,
                gender = request.gender,
                pageable = PageRequest.of(request.page, request.size),
                searchKeyword = request.searchKeyword,
                isDeleted = request.isDeleted,
            )
        }
    }
}
