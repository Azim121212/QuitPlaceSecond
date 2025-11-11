package com.example.quitplace.domain.model

import java.time.Instant
import java.util.UUID

// Категории проблем
enum class ProblemCategory(val displayName: String) {
    ANXIETY("Тревожность"),
    DEPRESSION("Депрессия"),
    RELATIONSHIPS("Отношения"),
    WORK_STRESS("Работа/Стресс"),
    SLEEP("Сон"),
    GENERAL("Общие проблемы")
}

// Язык поста
enum class Language(val code: String) {
    RUSSIAN("ru"),
    ENGLISH("en"),
    SPANISH("es");

    companion object {
        fun detectFromText(text: String): Language {
            return when {
                text.contains(Regex("[а-яА-Я]")) -> RUSSIAN
                text.contains(Regex("[a-zA-Z]")) -> ENGLISH
                else -> ENGLISH // по умолчанию английский
            }
        }
    }
}

// Основная сущность - пост
data class Post(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val language: Language = Language.ENGLISH,
    val category: ProblemCategory = ProblemCategory.GENERAL,
    val createdAt: Instant = Instant.now(),
    val authorId: String? = null // анонимный ID автора
) {
    init {
        require(content.isNotBlank()) { "Пост не может быть пустым" }
        require(content.length <= 5000) { "Пост слишком длинный" }
    }
}