package com.example.quitplace.data.network.model

data class DeepSeekRequest(
    val model: String = "deepseek-chat",
    val messages: List<Message>,
    val stream: Boolean = false
)

