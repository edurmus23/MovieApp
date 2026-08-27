package com.example.movieapp.domain.model

data class ChatRequest(
    val message: String,
    val language: String
)

data class ChatResponse(
    val reply: String,
    val user_id: String? = null,
    val movies: List<MovieDto>? = null,
    val lists: List<UserList>? = null
)

data class ChatMessage(
    val role: ChatRole,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val movies: List<MovieDto>? = null
)

enum class ChatRole {
    USER, AI
}
