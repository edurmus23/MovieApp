package com.example.movieapp.domain.model

data class UserList(
    val id: String,
    val name: String,
    val movieCount: Int = 0,
    val userId: String,
    val thumbnailPath: String? = null
)
