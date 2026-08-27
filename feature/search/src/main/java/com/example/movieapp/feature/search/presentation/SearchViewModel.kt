package com.example.movieapp.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.repository.MoviesRepository
import com.example.movieapp.domain.repository.SearchRepository
import com.example.movieapp.domain.util.RestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MoviesRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onStart() {
        if (_state.value.searchHistory.isEmpty()) {
            getSearchHistory()
        }
        if (_state.value.topRatedMovies.isEmpty()) {
            getTopRatedMovies()
        }
        if (_state.value.genres.isEmpty()) {
            getGenres()
        }
        // Trend aramalar için mock veriler
        if (_state.value.trendingSearches.isEmpty()) {
            _state.update { 
                it.copy(
                    trendingSearches = listOf("Batman", "Marvel", "Inception", "Avengers", "The Dark Knight", "Star Wars")
                )
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        _state.update { it.copy(query = newQuery) }
        
        searchJob?.cancel()
        if (newQuery.isBlank()) {
            _state.update { it.copy(movies = emptyList(), isLoading = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(500.milliseconds)
            searchMovies(newQuery)
        }
    }

    fun onSearch(query: String) {
        if (query.isBlank()) return
        
        viewModelScope.launch {
            searchRepository.insertSearch(query)
            searchMovies(query)
        }
    }

    fun onDeleteHistory(query: String) {
        viewModelScope.launch {
            searchRepository.deleteSearch(query)
        }
    }

    fun onClearAllHistory() {
        viewModelScope.launch {
            searchRepository.clearAllHistory()
        }
    }

    private fun searchMovies(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            when (val result = searchRepository.searchMovies(query)) {
                is RestResult.Success -> {
                    _state.update { 
                        it.copy(
                            movies = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
                is RestResult.Error -> {
                    _state.update { 
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    private fun getSearchHistory() {
        searchRepository.getSearchHistory()
            .onEach { history ->
                _state.update { it.copy(searchHistory = history) }
            }
            .launchIn(viewModelScope)
    }

    private fun getTopRatedMovies() {
        viewModelScope.launch {
            when (val result = movieRepository.getTopRatedMovies()) {
                is RestResult.Success -> {
                    _state.update { it.copy(topRatedMovies = result.data ?: emptyList()) }
                }
                else -> Unit
            }
        }
    }

    private fun getGenres() {
        viewModelScope.launch {
            when (val result = searchRepository.getGenres()) {
                is RestResult.Success -> {
                    val genreMap = result.data?.associate { it.id to it.name } ?: emptyMap()
                    _state.update { it.copy(genres = genreMap) }
                }
                else -> Unit
            }
        }
    }
}
