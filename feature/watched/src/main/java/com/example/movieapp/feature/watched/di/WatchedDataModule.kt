package com.example.movieapp.feature.watched.di

import com.example.movieapp.data.local.dao.WatchedMovieDao
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.repository.WatchedRepository
import com.example.movieapp.feature.watched.data.repository.WatchedRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WatchedDataModule {

    @Provides
    @Singleton
    fun provideWatchedRepository(
        dao: WatchedMovieDao,
        authRepository: AuthRepository,
        firestore: FirebaseFirestore
    ): WatchedRepository {
        return WatchedRepositoryImpl(dao, authRepository, firestore)
    }
}
