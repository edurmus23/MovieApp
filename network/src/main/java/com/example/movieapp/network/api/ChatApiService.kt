package com.example.movieapp.network.api

import com.example.movieapp.domain.model.ChatRequest
import com.example.movieapp.domain.model.ChatResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatApiService {
    @POST("api/chat")
    suspend fun sendMessage(
        @Header("Authorization") authorization: String,
        @Body request: ChatRequest
    ): ChatResponse
}
