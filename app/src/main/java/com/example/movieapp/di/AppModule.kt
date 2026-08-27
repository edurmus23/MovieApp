package com.example.movieapp.di

import android.app.Application
import androidx.room.Room
import com.example.movieapp.data.local.MovieDatabase
import com.example.movieapp.domain.repository.util.AppDatabase
import com.example.movieapp.feature.auth.data.local.dao.UserDao
import com.example.movieapp.data.local.dao.FavoriteMovieDao
import com.example.movieapp.data.local.dao.UserListDao
import com.example.movieapp.feature.search.data.local.dao.SearchHistoryDao
import com.example.movieapp.data.local.dao.RecentMovieDao
import com.example.movieapp.data.local.dao.WatchedMovieDao
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
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "movie_db",
        ).fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(db: MovieDatabase): AppDatabase = db

    @Provides
    @Singleton
    fun provideUserDao(db: MovieDatabase): UserDao = db.userDao

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance("gs://movieappfire.firebasestorage.app")

    @Provides
    @Singleton
    fun provideSearchHistoryDao(db: MovieDatabase): SearchHistoryDao = db.searchHistoryDao

    @Provides
    @Singleton
    fun provideFavouriteMovieDao(db: MovieDatabase): FavoriteMovieDao = db.favoriteMovieDao

    @Provides
    @Singleton
    fun provideUserListDao(db: MovieDatabase): UserListDao = db.userListDao

    @Provides
    @Singleton
    fun provideRecentMovieDao(db: MovieDatabase): RecentMovieDao = db.recentMovieDao

    @Provides
    @Singleton
    fun provideWatchedMovieDao(db: MovieDatabase): WatchedMovieDao = db.watchedMovieDao
}
