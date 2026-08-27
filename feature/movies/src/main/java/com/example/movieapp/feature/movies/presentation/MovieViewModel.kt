package com.example.movieapp.feature.movies.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.repository.FavouriteRepository
import com.example.movieapp.feature.movies.domain.usecase.GetBannerMoviesUseCase
import com.example.movieapp.feature.movies.domain.usecase.GetPopularMoviesUseCase
import com.example.movieapp.feature.movies.domain.usecase.GetTopRatedMoviesUseCase
import com.example.movieapp.feature.movies.domain.usecase.GetUpcomingMoviesUseCase
import com.example.movieapp.feature.movies.domain.usecase.ToggleFavoriteUseCase
import com.example.movieapp.feature.movies.R
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.movieapp.domain.util.RestResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getBannerMoviesUseCase: GetBannerMoviesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val favouriteRepository: FavouriteRepository,
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModel() {

    private val _authError = Channel<String>()
    val authError = _authError.receiveAsFlow()

    private val _bannerMovies = MutableStateFlow<List<MovieDto>>(emptyList())
    val bannerMovies = _bannerMovies.asStateFlow()

    val moviePagingData: Flow<PagingData<MovieDto>> = getPopularMoviesUseCase()
        .cachedIn(viewModelScope)

    val topRatedMovies: Flow<PagingData<MovieDto>> = getTopRatedMoviesUseCase()
        .cachedIn(viewModelScope)

    val upcomingMovies: Flow<PagingData<MovieDto>> = getUpcomingMoviesUseCase()
        .cachedIn(viewModelScope)

    val favouriteMovies: StateFlow<List<MovieDto>> = favouriteRepository.getFavouriteMovies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favouriteIds: StateFlow<Set<Int>> = favouriteMovies
        .map { movies -> movies.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    init {
        fetchBannerMovies()
    }

    private fun fetchBannerMovies() {
        viewModelScope.launch {
            when (val result = getBannerMoviesUseCase()) {
                is RestResult.Success -> {
                    _bannerMovies.value = result.data ?: emptyList()
                }
                is RestResult.Error -> {
                    // Log or handle error
                }
                else -> {}
            }
        }
    }

    fun onToggleFavourite(movie: MovieDto) {
        if (authRepository.currentUserId == null) {
            viewModelScope.launch {
                _authError.send(application.getString(R.string.movie_auth_error_favorite))
            }
            return
        }
        
        viewModelScope.launch {
            toggleFavoriteUseCase(movie)
        }
    }
}
