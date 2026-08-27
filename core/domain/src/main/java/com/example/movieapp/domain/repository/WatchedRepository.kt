package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.MovieDto
import kotlinx.coroutines.flow.Flow

interface WatchedRepository {
    suspend fun insertWatched(movie: MovieDto)
    suspend fun deleteWatched(movie: MovieDto)
    fun getWatchedMovies(): Flow<List<MovieDto>>
    fun isWatched(movieId: Int): Flow<Boolean>
    fun getWatchedMovieCount(): Flow<Int>
    suspend fun syncFromRemote()
}
