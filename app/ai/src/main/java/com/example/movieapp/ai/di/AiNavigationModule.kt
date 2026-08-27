package com.example.movieapp.ai.di

import com.example.movieapp.ai.presentation.AiChatScreen
import com.example.movieapp.navigation.AiChat
import com.example.movieapp.navigation.MovieDetail
import com.example.movieapp.navigation.EntryProviderInstaller
import com.example.movieapp.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object AiNavigationModule {

    @Provides
    @IntoSet
    fun provideAiEntries(
        navigator: Navigator
    ): EntryProviderInstaller = {
        entry<AiChat> {
            AiChatScreen(
                onBackClick = { navigator.goBack() },
                onMovieClick = { movieId ->
                    navigator.navigate(MovieDetail(movieId))
                }
            )
        }
    }
}
