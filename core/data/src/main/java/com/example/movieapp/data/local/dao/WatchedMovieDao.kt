package com.example.movieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.data.local.entity.WatchedMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchedMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatched(movie: WatchedMovieEntity)

    @Delete
    suspend fun deleteWatched(movie: WatchedMovieEntity)

    @Query("DELETE FROM watched_movies WHERE id = :movieId AND userId = :userId")
    suspend fun deleteWatchedById(movieId: Int, userId: String)

    @Query("SELECT * FROM watched_movies WHERE userId = :userId ORDER BY watchedAt DESC")
    fun getWatchedMovies(userId: String): Flow<List<WatchedMovieEntity>>

    @Query("SELECT EXISTS(SELECT * FROM watched_movies WHERE id = :movieId AND userId = :userId)")
    fun isWatched(movieId: Int, userId: String): Flow<Boolean>

    @Query("SELECT COUNT(*) FROM watched_movies WHERE userId = :userId")
    fun getWatchedMovieCount(userId: String): Flow<Int>
}
