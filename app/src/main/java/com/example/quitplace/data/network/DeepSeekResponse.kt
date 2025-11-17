package com.example.quitplace.data.network

import com.example.quitplace.data.network.model.Message

data class DeepSeekResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)