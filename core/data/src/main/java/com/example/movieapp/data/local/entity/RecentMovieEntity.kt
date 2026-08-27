package com.example.movieapp.data.local.entity

import androidx.room.Entity
import com.example.movieapp.domain.model.MovieDto

@Entity(tableName = "recent_movies", primaryKeys = ["id", "userId"])
data class RecentMovieEntity(
    val id: Int = 0,
    val userId: String = "",
    val title: String = "",
    val posterPath: String? = null,
    val voteAverage: Double = 0.0,
    val releaseDate: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toMovieDto() = MovieDto(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = "",
        releaseDate = releaseDate,
        voteAverage = voteAverage
    )
}
