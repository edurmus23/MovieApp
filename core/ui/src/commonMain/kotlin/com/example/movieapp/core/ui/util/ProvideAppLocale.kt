package com.example.movieapp.core.ui.util

import androidx.compose.runtime.Composable

@Composable
expect fun ProvideAppLocale(
    languageCode: String,
    content: @Composable () -> Unit
)
