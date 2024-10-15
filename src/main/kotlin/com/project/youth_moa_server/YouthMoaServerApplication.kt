package com.project.youth_moa_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class YouthMoaServerApplication

fun main(args: Array<String>) {
	runApplication<YouthMoaServerApplication>(*args)
}
