package com.example.movieapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "list_movie_cross_ref",
    primaryKeys = ["listId", "movieId"],
    indices = [Index(value = ["movieId"])]
)
data class ListMovieCrossRef(
    val listId: String,
    val movieId: Int
)
