package com.example.movieapp.feature.social.presentation.social

import com.example.movieapp.navigation.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object SocialNavigationModule {

    @Provides
    @IntoSet
    fun provideSocialEntries(
        navigator: Navigator
    ): EntryProviderInstaller = {
        entry<Social> {
            SocialScreen(
                onBackClick = { navigator.goBack() },
                onUserClick = { userId ->
                    navigator.navigate(PublicProfile(userId))
                }
            )
        }
    }
}
