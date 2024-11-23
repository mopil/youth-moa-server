package com.project.youthmoa.api.controller.common.response

import com.project.youthmoa.domain.model.YouthCenter

data class GetAllYouthCentersResponse(
    val youthCenters: List<YouthCenterResponse>,
)

data class YouthCenterResponse(
    val id: Long,
    val name: String,
    val region: String,
) {
    companion object {
        fun from(youthCenter: YouthCenter) =
            YouthCenterResponse(
                id = youthCenter.id,
                name = youthCenter.name,
                region = youthCenter.region,
            )
    }
}
