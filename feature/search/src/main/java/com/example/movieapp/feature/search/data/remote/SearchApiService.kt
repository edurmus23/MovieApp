package com.example.movieapp.feature.search.data.remote

import com.example.movieapp.domain.model.GenreResponseDto
import com.example.movieapp.domain.model.MovieResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): MovieResponseDto

    @GET("genre/movie/list")
    suspend fun getGenres(): GenreResponseDto
}
