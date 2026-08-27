package com.example.movieapp.feature.search.presentation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import com.example.movieapp.navigation.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object SearchNavigationModule {

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @Provides
    @IntoSet
    fun provideSearchEntry(
        navigator: Navigator
    ): EntryProviderInstaller = {

        entry<Search>(
            metadata = ListDetailSceneStrategy.listPane()
        ) {
            SearchScreen(
                onMovieClick = { movieId ->
                    navigator.navigate(MovieDetail(movieId))
                }
            )
        }
    }
}
