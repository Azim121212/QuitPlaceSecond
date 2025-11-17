package com.example.quitplace.data.repository

import com.example.quitplace.data.network.RetrofitInstance
import com.example.quitplace.data.network.model.DeepSeekRequest
import com.example.quitplace.data.network.model.Message
import com.example.quitplace.domain.repository.AIChatRepository

class AIChatRepositoryImpl : AIChatRepository {
    override suspend fun sendMessage(userMessage: String): String {
        return try {
            // Реальный запрос к DeepSeek API
            val request = DeepSeekRequest(
                messages = listOf(
                    Message(
                        role = "user",
                        content = """
                            Ты Bimbo - AI психолог-помощник в приложении для ментального здоровья.
                            Отвечай кратко, поддерживающе, по-русски.
                            Не давай медицинских диагнозов.
                            Запрос пользователя: $userMessage
                        """.trimIndent()
                    )
                )
            )

            // ИСПРАВЛЕННЫЙ ВЫЗОВ - убрал лишний параметр
            val response = RetrofitInstance.deepSeekApi.sendMessage(request)
            response.choices.first().message.content
        } catch (e: Exception) {
            "Извините, временные технические проблемы. Попробуйте позже."
        }
    }
}