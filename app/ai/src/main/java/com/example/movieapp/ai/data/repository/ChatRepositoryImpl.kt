package com.example.movieapp.ai.data.repository

import com.example.movieapp.domain.model.ChatRequest
import com.example.movieapp.domain.model.ChatResponse
import com.example.movieapp.domain.repository.ChatRepository
import com.example.movieapp.network.api.ChatApiService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApiService
) : ChatRepository {

    override suspend fun sendMessage(message: String): ChatResponse {
        val user = FirebaseAuth.getInstance().currentUser
            ?: throw Exception("Kullanıcı giriş yapmamış")

        val tokenResult = user.getIdToken(false).await()
        val token = tokenResult.token
            ?: throw Exception("Firebase token alınamadı")

        return api.sendMessage(
            authorization = "Bearer $token",
            request = ChatRequest(
                message = message,
                language = java.util.Locale.getDefault().language
            )
        )
    }
}
