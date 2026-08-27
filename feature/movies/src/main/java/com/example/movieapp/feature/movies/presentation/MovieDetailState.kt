package com.example.movieapp.feature.movies.presentation

import com.example.movieapp.domain.model.MovieDetailDto
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.WatchCountryProviderDto

data class MovieDetailState(
    val isLoading : Boolean = false ,
    val movie : MovieDetailDto?=null,
    val similarMovies: List<MovieDto> = emptyList(),
    val watchProviders: WatchCountryProviderDto? = null,
    val error : String?=null,
    val isFavorite: Boolean = false,
    val isWatched: Boolean = false,
    val userRating: Int = 0,
    val globalRating: Double = 0.0,
    val ratingCount: Long = 0,
    val trailerKey: String? = null,
    val isTrailerError: Boolean = false,
    val watchProvidersChecked: Boolean = false
)
