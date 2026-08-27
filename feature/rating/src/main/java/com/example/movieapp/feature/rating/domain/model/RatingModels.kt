package com.example.movieapp.feature.rating.domain.model

data class UserRating(
    val movieId: Int = 0,
    val rating: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val movieTitle: String = "",
    val moviePosterPath: String? = null,
    val movieVoteAverage: Double = 0.0
)

data class GlobalMovieRating(
    val movieId: Int = 0,
    val count: Int = 0,
    val sum: Double = 0.0,
    val average: Double = 0.0
)
