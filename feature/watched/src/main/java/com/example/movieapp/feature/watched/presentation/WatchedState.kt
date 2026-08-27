package com.example.movieapp.feature.watched.presentation

import com.example.movieapp.domain.model.MovieDto

data class WatchedState(
    val movies: List<MovieDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
