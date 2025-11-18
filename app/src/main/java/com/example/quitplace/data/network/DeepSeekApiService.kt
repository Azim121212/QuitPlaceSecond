package com.example.quitplace.data.network

import com.example.quitplace.data.network.model.DeepSeekRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface DeepSeekApiService {
    @POST("v1/chat/completions")
    suspend fun sendMessage(
        @Body request: DeepSeekRequest
    ): DeepSeekResponse
}