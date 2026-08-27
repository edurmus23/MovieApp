package com.example.movieapp.feature.auth.presentation

import com.example.movieapp.domain.util.RestResult

data class AuthState(
    val isLoading: Boolean = false,
    val error : String? = null,
    val isSuccess : Boolean = false

)
