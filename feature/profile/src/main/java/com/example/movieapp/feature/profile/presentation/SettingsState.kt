package com.example.movieapp.feature.profile.presentation

data class SettingsState(
    val currentName: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
