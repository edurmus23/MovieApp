package com.example.movieapp.feature.profile.domain.usecase

import com.example.movieapp.feature.profile.domain.model.ProfileData
import com.example.movieapp.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPublicProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(userId: String): Flow<ProfileData?> {
        return repository.getPublicProfile(userId)
    }
}
