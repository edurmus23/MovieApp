package com.example.movieapp.domain.model

data class User(
    val id: String? = null,
    val name: String,
    val email: String,
    val username: String = "",
    val profilePictureUrl: String? = null
)
