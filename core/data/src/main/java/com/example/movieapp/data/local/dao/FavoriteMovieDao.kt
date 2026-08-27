package com.example.movieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.data.local.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovieEntity)

    @Delete
    suspend fun deleteFavorite(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorites WHERE id = :movieId AND userId = :userId")
    suspend fun deleteFavoriteById(movieId: Int, userId: String)

    @Query("SELECT * FROM favorites WHERE userId = :userId")
    fun getFavoriteMovies(userId: String): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE id = :movieId AND userId = :userId)")
    fun isFavorite(movieId: Int, userId: String): Flow<Boolean>
}
