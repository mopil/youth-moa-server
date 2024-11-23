package com.project.youthmoa.common.util.file

import jakarta.servlet.http.HttpServletResponse
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * 엑셀 파일을 생성하는 인터페이스
 * - [createExcelWorkbook] 함수를 통해 엑셀 파일을 생성할 수 있다.
 * - dataList로 들어올 데이터 class는 [ExcelRow] 인터페이스를 구현해야 한다.
 * - [ExcelRow] 구현체의 property는 String 타입만 허용하고, [ExcelColumn] 어노테이션을 가져야 한다.
 */
interface ExcelManager {
    fun <T : ExcelRow> createExcelWorkbook(
        sheetName: String,
        dataList: List<T>,
    ): SXSSFWorkbook

    fun HttpServletResponse.setExcelDownload(
        fileName: String,
        workbook: SXSSFWorkbook,
    ) {
        apply {
            contentType = "ms-vnd/excel"
            setHeader("Content-Disposition", "attachment; filename=$fileName")
            workbook.write(outputStream)
            workbook.close()
        }
    }

    object Default : ExcelManager {
        override fun <T : ExcelRow> createExcelWorkbook(
            sheetName: String,
            dataList: List<T>,
        ): SXSSFWorkbook {
            require(dataList.isNotEmpty()) { "Data list cannot be empty." }
            val clazz = dataList.first()::class
            clazz.memberProperties.forEach { property ->
                // `String` 타입만 허용
                require(property.returnType.classifier == String::class) {
                    "Property ${property.name} must be of type String."
                }
                // `ExcelHeader` 애노테이션이 없으면 오류
                require(property.findAnnotation<ExcelColumn>() != null) {
                    "Property ${property.name} must be annotated with @ExcelHeader."
                }
            }

            val workbook = SXSSFWorkbook()
            val sheet = workbook.createSheet(sheetName)

            sheet.createHeaderRow(clazz)

            sheet.createDataRows(dataList)

            return workbook
        }

        private fun <T : Any> Sheet.createHeaderRow(clazz: KClass<T>) {
            val properties =
                clazz.memberProperties
                    .mapNotNull { property ->
                        property.findAnnotation<ExcelColumn>()?.let { annotation ->
                            property to annotation
                        }
                    }
                    .sortedBy { it.second.order } // `order`를 기준으로 정렬

            createRow(0).apply {
                properties.forEachIndexed { index, (property, annotation) ->
                    createCell(index).setCellValue(annotation.name)
                }
            }
        }

        private fun <T : Any> Sheet.createDataRows(data: List<T>) {
            data.forEachIndexed { rowIndex, item ->
                createRow(rowIndex + 1).apply {
                    item::class.memberProperties
                        .mapNotNull { property ->
                            property.findAnnotation<ExcelColumn>()?.let { annotation ->
                                property to annotation
                            }
                        }
                        .sortedBy { it.second.order }
                        .forEachIndexed { colIndex, (property, _) ->
                            createCell(colIndex).setCellValue(property.getter.call(item)?.toString() ?: "")
                        }
                }
            }
        }
    }
}
