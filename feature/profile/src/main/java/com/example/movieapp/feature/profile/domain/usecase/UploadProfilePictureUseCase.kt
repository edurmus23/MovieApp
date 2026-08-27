package com.example.movieapp.feature.profile.domain.usecase

import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.util.RestResult
import javax.inject.Inject

class UploadProfilePictureUseCase @Inject constructor(
    // Triggering re-compilation
    private val repository: AuthRepository
) {
    suspend operator fun invoke(uri: String): RestResult<String> {
        return repository.uploadProfilePicture(uri)
    }
}
