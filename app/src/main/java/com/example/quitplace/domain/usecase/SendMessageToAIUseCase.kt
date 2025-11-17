package com.example.quitplace.domain.usecase

import com.example.quitplace.domain.repository.AIChatRepository

class SendMessageToAIUseCase(
    private val aiChatRepository: AIChatRepository
) {
    suspend operator fun invoke(message: String): String {
        return aiChatRepository.sendMessage(message)
    }
}