package com.example.movieapp.feature.social.di

import com.example.movieapp.feature.social.data.repository.SocialRepositoryImpl
import com.example.movieapp.domain.repository.SocialRepository
import com.example.movieapp.feature.social.presentation.social.SocialViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val socialModule = module {
    single<SocialRepository> { SocialRepositoryImpl(get(), get()) }
    viewModelOf(::SocialViewModel)
}
