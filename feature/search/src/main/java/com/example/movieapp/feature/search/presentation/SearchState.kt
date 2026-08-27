package com.example.movieapp.feature.search.presentation

import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.SearchHistory

data class SearchState(
    val query: String = "",
    val movies: List<MovieDto> = emptyList(),
    val topRatedMovies: List<MovieDto> = emptyList(),
    val genres: Map<Int, String> = emptyMap(),
    val searchHistory: List<SearchHistory> = emptyList(),
    val trendingSearches: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
