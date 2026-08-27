package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.GenreDto
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.SearchHistory
import com.example.movieapp.domain.util.RestResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getSearchHistory(): Flow<List<SearchHistory>>
    suspend fun insertSearch(query: String)
    suspend fun deleteSearch(query: String)
    suspend fun clearAllHistory()
    
    suspend fun searchMovies(query: String): RestResult<List<MovieDto>>
    suspend fun getGenres(): RestResult<List<GenreDto>>
}
