package com.project.youthmoa.domain.model.converter

import jakarta.persistence.AttributeConverter

class CommaToStringListConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(attribute: List<String>): String {
        return attribute.joinToString(",")
    }

    override fun convertToEntityAttribute(dbData: String): List<String> {
        return dbData.split(",")
    }
}
