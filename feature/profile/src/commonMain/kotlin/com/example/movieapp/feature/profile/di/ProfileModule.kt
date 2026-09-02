package com.example.movieapp.feature.profile.di

import com.example.movieapp.feature.profile.data.repository.ProfileRepositoryImpl
import com.example.movieapp.feature.profile.domain.repository.ProfileRepository
import com.example.movieapp.feature.profile.domain.usecase.*
import com.example.movieapp.feature.profile.presentation.ProfileViewModel
import com.example.movieapp.feature.profile.presentation.SettingsViewModel
import com.example.movieapp.feature.profile.presentation.public_profile.PublicProfileViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get(), get(), get()) }
    
    factoryOf(::GetProfileUseCase)
    factoryOf(::GetPublicProfileUseCase)
    factoryOf(::UpdateProfileUseCase)
    factoryOf(::UpdatePasswordUseCase)
    factoryOf(::UploadProfilePictureUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::AddRecentMovieUseCase)
    
    viewModelOf(::ProfileViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::PublicProfileViewModel)
}
