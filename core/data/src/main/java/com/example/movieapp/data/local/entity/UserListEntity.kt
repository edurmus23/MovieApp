package com.example.movieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieapp.domain.model.UserList

@Entity(tableName = "user_lists")
data class UserListEntity(
    @PrimaryKey val id: String,
    val name: String,
    val userId: String,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toUserList(movieCount: Int = 0) = UserList(
        id = id,
        name = name,
        userId = userId,
        movieCount = movieCount
    )
}
