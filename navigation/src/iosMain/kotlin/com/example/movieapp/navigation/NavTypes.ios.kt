package com.example.movieapp.navigation

import androidx.compose.runtime.Composable

actual interface NavKey

actual class EntryProviderScope<T : NavKey> {
    actual fun <K : T> entry(
        metadata: Any?,
        content: @Composable (K) -> Unit
    ) {
    }
}
