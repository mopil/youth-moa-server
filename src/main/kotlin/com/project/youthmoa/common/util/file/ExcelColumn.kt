package com.project.youthmoa.common.util.file

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcelColumn(
    val order: Int,
    val name: String,
)
