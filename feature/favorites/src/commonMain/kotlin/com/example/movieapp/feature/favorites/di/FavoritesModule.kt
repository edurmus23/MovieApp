package com.example.movieapp.feature.favorites.di

import com.example.movieapp.domain.repository.FavouriteRepository
import com.example.movieapp.feature.favorites.data.repository.FavouriteRepositoryImpl
import org.koin.dsl.module

val favoritesModule = module {
    single<FavouriteRepository> {
        FavouriteRepositoryImpl(get(), get(), get(), get())
    }
}
