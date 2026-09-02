package com.example.movieapp.navigation

import androidx.compose.runtime.Composable

expect interface NavKey

expect class EntryProviderScope<T : NavKey> {
    fun <K : T> entry(
        metadata: Any? = null,
        content: @Composable (K) -> Unit
    )
}
