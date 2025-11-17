package com.example.quitplace.domain.model

data class DeepSeekResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

data class Message(
    val content: String
)