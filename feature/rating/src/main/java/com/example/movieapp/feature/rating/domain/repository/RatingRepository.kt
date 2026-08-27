package com.example.movieapp.feature.rating.domain.repository

import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.feature.rating.domain.model.GlobalMovieRating
import com.example.movieapp.feature.rating.domain.model.UserRating
import kotlinx.coroutines.flow.Flow

interface RatingRepository {
    suspend fun submitRating(movie: MovieDto, rating: Int)
    fun getUserRating(movieId: Int): Flow<UserRating?>
    fun getGlobalRating(movieId: Int): Flow<GlobalMovieRating?>
    fun getUserRatingsCount(userId: String): Flow<Int>
    fun getUserAverageRating(userId: String): Flow<Double>
    fun getRatedMovies(userId: String): Flow<List<UserRating>>
}
