package com.example.movieapp.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect

@Composable
actual fun ProvideAppLocale(
    languageCode: String,
    content: @Composable () -> Unit
) {
    SideEffect {
        changeAppLanguage(languageCode)
    }
    content()
}
