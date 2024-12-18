package com.project.youthmoa.domain.model.converter

import jakarta.persistence.AttributeConverter

class CommaToLongListConverter : AttributeConverter<List<Long>, String?> {
    override fun convertToDatabaseColumn(attribute: List<Long>): String? {
        if (attribute.isEmpty()) {
            return null
        }
        return attribute.joinToString(",")
    }

    override fun convertToEntityAttribute(dbData: String?): List<Long> {
        return dbData?.split(",")?.map { it.toLong() } ?: emptyList()
    }
}
