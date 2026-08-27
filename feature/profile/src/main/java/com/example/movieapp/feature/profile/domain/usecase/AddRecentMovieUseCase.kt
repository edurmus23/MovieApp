package com.example.movieapp.feature.profile.domain.usecase

import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class AddRecentMovieUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(movie: MovieDto) {
        authRepository.currentUserId?.let { userId ->
            profileRepository.addRecentMovie(userId, movie)
        }
    }
}
