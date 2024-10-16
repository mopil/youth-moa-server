package com.project.youthmoa.domain.repository

import com.project.youthmoa.domain.model.YouthCenter
import org.springframework.data.jpa.repository.JpaRepository

interface YouthCenterRepository : JpaRepository<YouthCenter, Long>
