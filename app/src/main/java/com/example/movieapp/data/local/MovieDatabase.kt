package com.example.movieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieapp.domain.repository.util.AppDatabase
import com.example.movieapp.feature.auth.data.local.dao.UserDao
import com.example.movieapp.feature.auth.data.local.entity.UserEntity
import com.example.movieapp.data.local.dao.FavoriteMovieDao
import com.example.movieapp.data.local.dao.UserListDao
import com.example.movieapp.data.local.entity.FavoriteMovieEntity
import com.example.movieapp.data.local.entity.ListMovieCrossRef
import com.example.movieapp.data.local.entity.UserListEntity
import com.example.movieapp.feature.search.data.local.dao.SearchHistoryDao
import com.example.movieapp.feature.search.data.local.entity.SearchHistoryEntity
import com.example.movieapp.data.local.dao.RecentMovieDao
import com.example.movieapp.data.local.dao.WatchedMovieDao
import com.example.movieapp.data.local.entity.RecentMovieEntity
import com.example.movieapp.data.local.entity.WatchedMovieEntity

@Database(
    entities = [
        UserEntity::class,
        SearchHistoryEntity::class,
        FavoriteMovieEntity::class,
        UserListEntity::class,
        ListMovieCrossRef::class,
        RecentMovieEntity::class,
        WatchedMovieEntity::class
    ],
    version = 9,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase(), AppDatabase {
    abstract val userDao: UserDao
    abstract val searchHistoryDao : SearchHistoryDao
    abstract val favoriteMovieDao : FavoriteMovieDao
    abstract val userListDao: UserListDao
    abstract val recentMovieDao: RecentMovieDao
    abstract val watchedMovieDao: WatchedMovieDao

    override fun clearAllData() {
        this.clearAllTables()
    }
}
