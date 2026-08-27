package com.example.movieapp.feature.movies.di

import com.example.movieapp.feature.movies.presentation.MovieDetailScreen
import com.example.movieapp.feature.movies.presentation.MoviesScreen

import com.example.movieapp.navigation.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowWidthSizeClass

@Module
@InstallIn(ActivityRetainedComponent::class)
object MoviesNavigationModule {

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @Provides
    @IntoSet
    fun provideMoviesEntry(
        navigator: Navigator
    ): EntryProviderInstaller = {

        entry<Movies>(
            metadata = ListDetailSceneStrategy.listPane()
        ) {
            MoviesScreen(
                onMovieClick = { movieId ->
                    navigator.navigate(MovieDetail(movieId))
                },
                onAiChatClick = {
                    navigator.navigate(AiChat)
                }
            )
        }

        entry<MovieDetail>(
            metadata = ListDetailSceneStrategy.detailPane()
        ) { key ->
            MovieDetailScreen(
                movieId = key.movieId,
                onBackClick = { navigator.goBack() },
                onMovieClick = { movieId ->
                    navigator.navigate(MovieDetail(movieId))
                }
            )
        }
    }
}
