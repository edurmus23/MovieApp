package com.example.movieapp.feature.auth.di

import com.example.movieapp.domain.repository.util.AppDatabase
import com.example.movieapp.feature.auth.data.local.util.SessionManager
import com.example.movieapp.feature.auth.data.repository.AuthRepositoryImpl
import com.example.movieapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDataModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        sessionManager: SessionManager,
        database: AppDatabase,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, sessionManager, database, firestore, storage)
    }
}
