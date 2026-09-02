package com.example.movieapp.data.di

import com.example.movieapp.data.local.MovieDatabase
import com.example.movieapp.data.local.createDatabase
import com.example.movieapp.data.local.platformDatabaseModule
import com.example.movieapp.domain.repository.util.AppDatabase
import org.koin.dsl.module

val dataModule = module {
    includes(platformDatabaseModule)
    
    single<MovieDatabase> { createDatabase(get()) }
    single<AppDatabase> { get<MovieDatabase>() }
    
    single { get<MovieDatabase>().userDao }
    single { get<MovieDatabase>().searchHistoryDao }
    single { get<MovieDatabase>().favoriteMovieDao }
    single { get<MovieDatabase>().userListDao }
    single { get<MovieDatabase>().recentMovieDao }
    single { get<MovieDatabase>().watchedMovieDao }
}
