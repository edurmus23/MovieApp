package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.ChatResponse

interface ChatRepository {
    suspend fun sendMessage(message: String): ChatResponse
}
