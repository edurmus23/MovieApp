package com.example.movieapp.feature.favorites

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
object FavoritesNavigationModule {

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @Provides
    @IntoSet
    fun provideFavoritesEntries(
        navigator: Navigator
    ): EntryProviderInstaller = {

        entry<Favorites>(
            metadata = ListDetailSceneStrategy.listPane()
        ) { key: Favorites ->
            FavoritesScreen(
                initialTab = key.initialTab,
                onMovieClick = { movieId ->
                    navigator.navigate(MovieDetail(movieId))
                },
                onListClick = { listId, listName ->
                    // Current user's own lists
                    navigator.navigate(ListDetail(listId, listName, "")) 
                }
            )
        }

        entry<MyLists>(
            metadata = ListDetailSceneStrategy.listPane()
        ) {
            MyListsScreen(
                onBackClick = { navigator.goBack() },
                onListClick = { listId, listName ->
                    navigator.navigate(ListDetail(listId, listName, ""))
                },
                onFavoritesClick = {
                    navigator.navigate(Favorites(0))
                }
            )
        }

        entry<ListDetail>(
            metadata = ListDetailSceneStrategy.detailPane()
        ) { key: ListDetail ->
            ListDetailScreen(
                listId = key.listId,
                listName = key.listName,
                userId = key.userId,
                onMovieClick = { movieId ->
                    navigator.navigate(MovieDetail(movieId))
                },
                onBackClick = { navigator.goBack() }
            )
        }
    }
}
