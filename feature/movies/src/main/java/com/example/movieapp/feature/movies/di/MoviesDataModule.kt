package com.example.movieapp.feature.movies.di

import com.example.movieapp.domain.repository.MoviesRepository
import com.example.movieapp.feature.movies.data.remote.MoviesApiService
import com.example.movieapp.feature.movies.data.repository.MoviesRepositoryImpl
import com.example.movieapp.network.di.TmdbRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoviesDataModule {

    @Provides
    @Singleton
    fun provideMoviesApiService(@TmdbRetrofit retrofit: Retrofit): MoviesApiService {
        return retrofit.create(MoviesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMoviesRepository(apiService: MoviesApiService): MoviesRepository {
        return MoviesRepositoryImpl(apiService)
    }
}
