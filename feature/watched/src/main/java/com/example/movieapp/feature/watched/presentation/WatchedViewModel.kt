package com.example.movieapp.feature.watched.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.repository.WatchedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchedViewModel @Inject constructor(
    private val watchedRepository: WatchedRepository
) : ViewModel() {

    private val _state = mutableStateOf(WatchedState())
    val state: State<WatchedState> = _state

    init {
        getWatchedMovies()
    }

    private fun getWatchedMovies() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            watchedRepository.getWatchedMovies().collectLatest { movies ->
                _state.value = _state.value.copy(
                    movies = movies,
                    isLoading = false
                )
            }
        }
    }
}
