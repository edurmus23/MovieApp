package com.example.movieapp.domain.repository

import androidx.paging.PagingData
import com.example.movieapp.domain.model.MovieDetailDto
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.WatchCountryProviderDto
import com.example.movieapp.domain.util.RestResult
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getPopularMovies(): Flow<PagingData<MovieDto>>
    suspend fun getBannerMovies(): RestResult<List<MovieDto>>

    suspend fun getMovieDetails(movieId: Int): RestResult<MovieDetailDto>

    suspend fun getMovieTrailer(movieId: Int): RestResult<String?>

    suspend fun getWatchProviders(movieId: Int): RestResult<WatchCountryProviderDto?>

    fun getTopRatedMoviesPaged(): Flow<PagingData<MovieDto>>
    suspend fun getTopRatedMovies(): RestResult<List<MovieDto>>

    fun getUpcomingMoviesPaged(): Flow<PagingData<MovieDto>>
    suspend fun getUpcomingMovies(): RestResult<List<MovieDto>>

    suspend fun getSimilarMovies(movieId : Int): RestResult<List<MovieDto>>
}
