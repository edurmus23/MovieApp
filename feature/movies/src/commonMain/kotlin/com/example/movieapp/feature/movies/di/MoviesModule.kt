package com.example.movieapp.feature.movies.di

import com.example.movieapp.domain.repository.MoviesRepository
import com.example.movieapp.feature.movies.data.remote.KtorMoviesApiService
import com.example.movieapp.feature.movies.data.remote.MoviesApiService
import com.example.movieapp.feature.movies.data.repository.MoviesRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val moviesModule = module {
    single<MoviesApiService> { 
        KtorMoviesApiService(get(named("TmdbClient"))) 
    }
    
    single<MoviesRepository> { 
        MoviesRepositoryImpl(get()) 
    }
}
