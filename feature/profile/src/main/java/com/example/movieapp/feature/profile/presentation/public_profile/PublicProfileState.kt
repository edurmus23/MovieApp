package com.example.movieapp.feature.profile.presentation.public_profile

import com.example.movieapp.feature.profile.domain.model.ProfileData

data class PublicProfileState(
    val profileData: ProfileData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
