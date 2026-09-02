package com.example.movieapp.feature.search.di

import com.example.movieapp.domain.repository.SearchRepository
import com.example.movieapp.feature.search.data.remote.KtorSearchApiService
import com.example.movieapp.feature.search.data.remote.SearchApiService
import com.example.movieapp.feature.search.data.repository.SearchRepositoryImpl
import com.example.movieapp.feature.search.presentation.SearchViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchModule = module {
    single<SearchApiService> { 
        KtorSearchApiService(get(named("TmdbClient"))) 
    }
    
    single<SearchRepository> { 
        SearchRepositoryImpl(get(), get()) 
    }
    
    viewModelOf(::SearchViewModel)
}
