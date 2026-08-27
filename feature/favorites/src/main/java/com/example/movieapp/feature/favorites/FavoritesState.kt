package com.example.movieapp.feature.favorites

import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.UserList

data class FavoritesState(
    val isLoading: Boolean = true,
    val movies: List<MovieDto> = emptyList(),
    val userLists: List<UserList> = emptyList(),
    val totalSavedCount: Int = 0,
    val error: String? = null
)

