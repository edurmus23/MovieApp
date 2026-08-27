package com.example.movieapp.feature.profile.domain.repository

import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.feature.profile.domain.model.ProfileData
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getRecentMovies(userId: String): Flow<List<MovieDto>>
    suspend fun addRecentMovie(userId: String, movie: MovieDto)
    fun getPublicProfile(userId: String): Flow<ProfileData?>
}
