package com.example.movieapp.feature.auth.di

import com.example.movieapp.feature.auth.presentation.login.LoginScreen
import com.example.movieapp.feature.auth.presentation.register.RegisterScreen
import com.example.movieapp.navigation.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.EntryProviderScope

@Module
@InstallIn(ActivityRetainedComponent::class)
object AuthNavigationModule {

    @Provides
    @IntoSet
    fun provideAuthEntries(
        navigator: Navigator
    ): EntryProviderInstaller = {

        entry<Login> {
            LoginScreen(
                onNavigateToRegister = {
                    navigator.navigate(Register)
                },
                onLoginSuccess = {
                    navigator.navigateAndClear(Movies)
                },
                onSkipClick = {
                    navigator.navigateAndClear(Movies)
                }
            )
        }

        entry<Register> {
            RegisterScreen(
                onNavigateToLogin = navigator::goBack,
                onRegisterSuccess = navigator::goBack
            )
        }
    }
}
