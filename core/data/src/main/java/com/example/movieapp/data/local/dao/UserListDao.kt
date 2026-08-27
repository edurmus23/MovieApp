package com.example.movieapp.data.local.dao

import androidx.room.*
import com.example.movieapp.data.local.entity.FavoriteMovieEntity
import com.example.movieapp.data.local.entity.ListMovieCrossRef
import com.example.movieapp.data.local.entity.UserListEntity
import com.example.movieapp.data.local.entity.UserListWithMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface UserListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: UserListEntity)

    @Query("SELECT * FROM user_lists WHERE userId = :userId ORDER BY createdAt DESC")
    fun getUserLists(userId: String): Flow<List<UserListEntity>>

    @Query("""
        SELECT 
            ul.*, 
            COUNT(lmc.movieId) as movieCount,
            (SELECT posterPath FROM favorites f JOIN list_movie_cross_ref lmc2 ON f.id = lmc2.movieId WHERE lmc2.listId = ul.id LIMIT 1) as thumbnailPath
        FROM user_lists ul
        LEFT JOIN list_movie_cross_ref lmc ON ul.id = lmc.listId
        WHERE ul.userId = :userId
        GROUP BY ul.id
        ORDER BY ul.createdAt DESC
    """)
    fun getUserListsWithMetadata(userId: String): Flow<List<UserListWithMetadata>>

    @Delete
    suspend fun deleteList(list: UserListEntity)

    @Query("DELETE FROM user_lists WHERE id = :listId")
    suspend fun deleteListById(listId: String)

    @Query("DELETE FROM list_movie_cross_ref WHERE listId = :listId")
    suspend fun deleteCrossRefsByListId(listId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieToList(crossRef: ListMovieCrossRef)

    @Query("""
        SELECT * FROM favorites 
        WHERE id IN (
            SELECT movieId FROM list_movie_cross_ref 
            WHERE listId = :listId
        )
    """)
    fun getMoviesInList(listId: String): Flow<List<FavoriteMovieEntity>>

    @Query("DELETE FROM list_movie_cross_ref WHERE listId = :listId AND movieId = :movieId")
    suspend fun removeMovieFromList(listId: String, movieId: Int)

    @Query("""
        SELECT COUNT(*) FROM (
            SELECT id FROM favorites WHERE userId = :userId
            UNION
            SELECT movieId FROM list_movie_cross_ref WHERE listId IN (SELECT id FROM user_lists WHERE userId = :userId)
        )
    """)
    fun getWatchlistMovieCount(userId: String): Flow<Int>

    @Transaction
    suspend fun deleteListWithMovies(listId: String) {
        deleteCrossRefsByListId(listId)
        deleteListById(listId)
    }
}
