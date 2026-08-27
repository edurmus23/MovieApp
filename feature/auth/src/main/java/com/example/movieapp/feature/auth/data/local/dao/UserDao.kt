package com.example.movieapp.feature.auth.data.local.dao

import androidx.room.*
import com.example.movieapp.feature.auth.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Delete
    suspend fun deleteUser(user: UserEntity)
}
