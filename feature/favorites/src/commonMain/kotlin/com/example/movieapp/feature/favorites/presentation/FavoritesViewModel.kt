package com.example.movieapp.feature.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favouriteRepository: FavouriteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state = _state.asStateFlow()

    init {
        getFavouriteMovies()
        getUserLists()
        getTotalSavedCount()
    }

    private fun getFavouriteMovies() {
        favouriteRepository.getFavouriteMovies()
            .onEach { movies ->
                _state.update { 
                    it.copy(
                        isLoading = false,
                        movies = movies
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getUserLists() {
        favouriteRepository.getUserLists()
            .onEach { lists ->
                _state.update { 
                    it.copy(userLists = lists)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getTotalSavedCount() {
        favouriteRepository.getTotalSavedMovieCount()
            .onEach { count ->
                _state.update { 
                    it.copy(totalSavedCount = count)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onCreateList(name: String) {
        viewModelScope.launch {
            favouriteRepository.createList(name)
        }
    }

    fun onDeleteList(listId: String) {
        viewModelScope.launch {
            favouriteRepository.deleteList(listId)
        }
    }

    fun onRemoveFavourite(movie: MovieDto) {
        viewModelScope.launch {
            favouriteRepository.deleteFavourite(movie)
        }
    }

    fun getMoviesInList(userId: String, listId: String) = favouriteRepository.getMoviesInList(userId, listId)

    fun onRemoveMovieFromList(listId: String, movieId: Int) {
        viewModelScope.launch {
            favouriteRepository.removeMovieFromList(listId, movieId)
        }
    }
}
