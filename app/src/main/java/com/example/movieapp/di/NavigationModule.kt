package com.example.movieapp.di

import com.example.movieapp.navigation.*
import com.example.movieapp.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object NavigationModule {

    @Provides
    @ActivityRetainedScoped
    fun provideNavigator(authRepository: AuthRepository): Navigator {
        val initialKey = if (authRepository.currentUserId != null) Movies else Login
        return Navigator(initialKey)
    }
}
