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
    GENERAL("Общие"),
    FEAR("Страх"),
    OTHER("Другие проблемы")
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

// Добавь в начало файла (после enum Language)
enum class TriggerWarning(val displayName: String, val emoji: String) {
    SELF_HARM("Самоповреждение", "⚠️"),
    VIOLENCE("Насилие", "🔞"),
    ABUSE("Абьюз", "🚫"),
    EATING_DISORDER("РПП", "🍽️"),
    SUBSTANCE_ABUSE("Зависимости", "💊"),
    SUICIDE("Суицид", "🆘"),
    TRAUMA("Травма", "💔")
}

// Основная сущность - пост
data class Post(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val language: Language = Language.ENGLISH,
    val category: ProblemCategory = ProblemCategory.GENERAL,
    val triggerWarnings: Set<TriggerWarning> = emptySet(), // ДОБАВЬ ЭТУ СТРОЧКУ
    val createdAt: Instant = Instant.now(),
    val authorId: String? = null
) {
    init {
        require(content.isNotBlank()) { "Пост не может быть пустым" }
        require(content.length <= 5000) { "Пост слишком длинный" }
    }
}