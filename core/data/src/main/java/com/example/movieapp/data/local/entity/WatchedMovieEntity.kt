package com.example.movieapp.data.local.entity

import androidx.room.Entity
import com.example.movieapp.domain.model.MovieDto

@Entity(tableName = "watched_movies", primaryKeys = ["id", "userId"])
data class WatchedMovieEntity(
    val id: Int,
    val userId: String,
    val title: String,
    val posterPath: String?,
    val voteAverage: Double,
    val releaseDate: String?,
    val genreIds: String? = null,
    val watchedAt: Long = System.currentTimeMillis()
) {
    fun toMovieDto() = MovieDto(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = "",
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        genreIds = genreIds?.split(",")?.filter { it.isNotBlank() }?.mapNotNull { it.toIntOrNull() } ?: emptyList()
    )
}
