package com.example.movieapp.domain.model

import com.google.gson.annotations.SerializedName

data class MovieDto(
    val id: Int = 0,
    val title: String = "",
    @SerializedName(value = "poster_path", alternate = ["posterPath"])
    val posterPath: String? = null,
    @SerializedName(value = "backdrop_path", alternate = ["backdropPath"])
    val backdropPath: String? = null,
    @SerializedName("overview")
    val overview: String = "",
    @SerializedName(value = "release_date", alternate = ["releaseDate"])
    val releaseDate: String? = null,
    @SerializedName(value = "vote_average", alternate = ["voteAverage"])
    val voteAverage: Double = 0.0,
    @SerializedName(value = "genre_ids", alternate = ["genreIds"])
    val genreIds: List<Int> = emptyList()
)
