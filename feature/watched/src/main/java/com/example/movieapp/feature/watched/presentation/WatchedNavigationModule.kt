package com.example.movieapp.feature.watched.presentation

import com.example.movieapp.navigation.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object WatchedNavigationModule {

    @Provides
    @IntoSet
    fun provideWatchedEntryProvider(
        navigator: Navigator
    ): EntryProviderInstaller = {
        entry<WatchedMovies> {
            WatchedScreen(
                onMovieClick = { movieId ->
                    navigator.navigate(MovieDetail(movieId))
                },
                onBackClick = {
                    navigator.goBack()
                }
            )
        }
    }
}
