package com.example.movieapp.feature.social.di

import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.repository.SocialRepository
import com.example.movieapp.feature.social.data.repository.SocialRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocialDataModule {

    @Provides
    @Singleton
    fun provideSocialRepository(
        firestore: FirebaseFirestore,
        authRepository: AuthRepository
    ): SocialRepository {
        return SocialRepositoryImpl(firestore, authRepository)
    }
}
