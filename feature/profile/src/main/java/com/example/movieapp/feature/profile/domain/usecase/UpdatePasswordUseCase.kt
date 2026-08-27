package com.example.movieapp.feature.profile.domain.usecase

import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.util.RestResult
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(password: String): RestResult<Unit> {
        return authRepository.updatePassword(password)
    }
}
