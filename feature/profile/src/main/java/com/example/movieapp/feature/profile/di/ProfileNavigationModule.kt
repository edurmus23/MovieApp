package com.example.movieapp.feature.profile.di

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import com.example.movieapp.feature.profile.presentation.ProfileScreen
import com.example.movieapp.feature.profile.presentation.SettingsScreen
import com.example.movieapp.feature.profile.presentation.public_profile.PublicProfileScreen
import com.example.movieapp.feature.profile.presentation.recent.RecentMoviesScreen
import com.example.movieapp.feature.rating.presentation.RatedMoviesScreen
import com.example.movieapp.navigation.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object ProfileNavigationModule {

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @Provides
    @IntoSet
    fun provideProfileEntries(
        navigator: Navigator
    ): EntryProviderInstaller = {
        entry<Profile>(
            metadata = ListDetailSceneStrategy.listPane()
        ) {
            ProfileScreen(
                onNavigateToLogin = {
                    navigator.navigate(Login)
                },
                onNavigateToSettings = {
                    navigator.navigate(Settings)
                },
                onNavigateToRecent = {
                    navigator.navigate(RecentMovies)
                },
                onNavigateToWatched = {
                    navigator.navigate(WatchedMovies)
                },
                onNavigateToSocial = {
                    navigator.navigate(Social)
                },
                onNavigateToFavorites = { tabIndex ->
                    if (tabIndex == 1) {
                        navigator.navigate(MyLists)
                    } else {
                        navigator.navigate(Favorites(tabIndex))
                    }
                },
                onNavigateToRatedMovies = { userId ->
                    navigator.navigate(RatedMovies(userId))
                },
                onMovieClick = { movieId ->
                    navigator.navigate(MovieDetail(movieId))
                },
                onNavigateToPublicProfile= {userId->
                    navigator.navigate(PublicProfile(userId ))
                }
            )
        }

        entry<PublicProfile>(
            metadata = ListDetailSceneStrategy.detailPane()
        ) { key: PublicProfile ->
            PublicProfileScreen(
                userId = key.userId,
                onBackClick = { navigator.goBack() },
                onMovieClick = { movieId ->
                    navigator.navigate(MovieDetail(movieId))
                },
                onListClick = { listId, listName, userId ->
                    navigator.navigate(ListDetail(listId, listName, userId))
                },
                onNavigateToRatedMovies = { userId ->
                    navigator.navigate(RatedMovies(userId))
                }
            )
        }

        entry<Settings>(
            metadata = ListDetailSceneStrategy.detailPane()
        ) {
            SettingsScreen(
                onBackClick = { navigator.goBack() }
            )
        }

        entry<RecentMovies>(
            metadata = ListDetailSceneStrategy.listPane()
        ) {
            RecentMoviesScreen(
                onBackClick = { navigator.goBack() },
                onMovieClick = { movieId ->
                    navigator.navigate(MovieDetail(movieId))
                }
            )
        }

        entry<RatedMovies>(
            metadata = ListDetailSceneStrategy.listPane()
        ) { key: RatedMovies ->
            RatedMoviesScreen(
                userId = key.userId,
                onMovieClick = { movieId ->
                    navigator.navigate(MovieDetail(movieId))
                },
                onBackClick = { navigator.goBack() }
            )
        }
    }
}
