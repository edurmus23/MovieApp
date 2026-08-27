package com.example.movieapp.feature.profile.domain.usecase

import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.util.RestResult
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String): RestResult<Unit> {
        return authRepository.updateProfile(name)
    }
}
