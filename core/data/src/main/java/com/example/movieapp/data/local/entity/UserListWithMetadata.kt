package com.example.movieapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.example.movieapp.domain.model.UserList

data class UserListWithMetadata(
    @Embedded val list: UserListEntity,
    @ColumnInfo(name = "movieCount") val movieCount: Int,
    @ColumnInfo(name = "thumbnailPath") val thumbnailPath: String?
) {
    fun toUserList() = UserList(
        id = list.id,
        name = list.name,
        userId = list.userId,
        movieCount = movieCount,
        thumbnailPath = thumbnailPath
    )
}
