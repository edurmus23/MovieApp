package com.example.movieapp.feature.watched.di

import com.example.movieapp.feature.watched.data.repository.WatchedRepositoryImpl
import com.example.movieapp.domain.repository.WatchedRepository
import com.example.movieapp.feature.watched.presentation.WatchedViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val watchedModule = module {
    single<WatchedRepository> { WatchedRepositoryImpl(get(), get(), get()) }
    viewModelOf(::WatchedViewModel)
}
