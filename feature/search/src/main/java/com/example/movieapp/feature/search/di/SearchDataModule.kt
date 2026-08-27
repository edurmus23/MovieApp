package com.example.movieapp.feature.search.di

import com.example.movieapp.feature.search.data.local.dao.SearchHistoryDao
import com.example.movieapp.feature.search.data.remote.SearchApiService
import com.example.movieapp.feature.search.data.repository.SearchRepositoryImpl
import com.example.movieapp.domain.repository.SearchRepository
import com.example.movieapp.network.di.TmdbRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchDataModule {

    @Provides
    @Singleton
    fun provideSearchApiService(@TmdbRetrofit retrofit: Retrofit): SearchApiService {
        return retrofit.create(SearchApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(
        dao: SearchHistoryDao,
        apiService: SearchApiService
    ): SearchRepository {
        return SearchRepositoryImpl(dao, apiService)
    }
}
