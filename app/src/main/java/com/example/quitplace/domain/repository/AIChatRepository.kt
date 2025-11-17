package com.example.quitplace.domain.repository

import com.example.quitplace.domain.model.DeepSeekResponse

interface AIChatRepository {
    suspend fun sendMessage(message: String): String
}