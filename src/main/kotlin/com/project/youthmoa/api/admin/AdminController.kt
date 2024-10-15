package com.project.youthmoa.api.admin

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "어드민")
@RestController
@RequestMapping("/admin")
class AdminController
