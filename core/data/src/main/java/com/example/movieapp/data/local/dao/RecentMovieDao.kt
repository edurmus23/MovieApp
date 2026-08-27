package com.example.movieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.data.local.entity.RecentMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentMovie(movie: RecentMovieEntity)

    @Query("SELECT * FROM recent_movies WHERE userId = :userId ORDER BY timestamp DESC LIMIT 10")
    fun getRecentMovies(userId: String): Flow<List<RecentMovieEntity>>
}
