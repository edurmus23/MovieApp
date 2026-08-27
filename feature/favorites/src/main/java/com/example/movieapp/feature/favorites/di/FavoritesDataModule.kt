package com.example.movieapp.feature.favorites.di

import com.example.movieapp.data.local.dao.FavoriteMovieDao
import com.example.movieapp.data.local.dao.UserListDao
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.repository.FavouriteRepository
import com.example.movieapp.feature.favorites.data.repository.FavouriteRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoritesDataModule {

    @Provides
    @Singleton
    fun provideFavouriteRepository(
        dao: FavoriteMovieDao,
        userListDao: UserListDao,
        authRepository: AuthRepository,
        firestore: FirebaseFirestore
    ): FavouriteRepository {
        return FavouriteRepositoryImpl(dao, userListDao, authRepository, firestore)
    }
}
