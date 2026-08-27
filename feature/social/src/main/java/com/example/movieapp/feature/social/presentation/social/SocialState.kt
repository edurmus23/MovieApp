package com.example.movieapp.feature.social.presentation.social

import com.example.movieapp.domain.model.User

data class SocialState(
    val searchResults: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
