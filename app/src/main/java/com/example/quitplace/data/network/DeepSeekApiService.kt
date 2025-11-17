package com.example.quitplace.data.network

import com.example.quitplace.data.network.model.DeepSeekRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface DeepSeekApiService {
    @POST("chat/completions")
    suspend fun sendMessage(
        @Body request: DeepSeekRequest  // УБРАЛ @Header - он уже в Interceptor
    ): DeepSeekResponse
}