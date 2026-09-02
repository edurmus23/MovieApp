package com.example.movieapp.feature.movies.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MovieTrailerPlayer(
    youtubeVideoId: String,
    modifier: Modifier = Modifier,
    onError: () -> Unit = {}
)

internal fun isValidYouTubeVideoId(videoId: String): Boolean =
    videoId.trim().matches(Regex("^[a-zA-Z0-9_-]{11}$"))
