package com.example.movieapp.feature.movies.presentation

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.repository.FavouriteRepository
import com.example.movieapp.domain.repository.WatchedRepository
import com.example.movieapp.domain.util.RestResult
import com.example.movieapp.domain.model.UserList
import com.example.movieapp.feature.movies.domain.usecase.*
import com.example.movieapp.feature.profile.domain.usecase.AddRecentMovieUseCase
import com.example.movieapp.feature.movies.R
import com.example.movieapp.feature.rating.domain.repository.RatingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieTrailerUseCase: GetMovieTrailerUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val getWatchProvidersUseCase: GetWatchProvidersUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val addMovieToListUseCase: AddMovieToListUseCase,
    private val addRecentMovieUseCase: AddRecentMovieUseCase,
    private val favouriteRepository: FavouriteRepository,
    private val ratingRepository: RatingRepository,
    private val watchedRepository: WatchedRepository,
    private val authRepository: AuthRepository,
    private val application: Application
): ViewModel() {
    private val _state = mutableStateOf(MovieDetailState())
    val state: State<MovieDetailState> = _state

    private val _authError = Channel<String>()
    val authError = _authError.receiveAsFlow()

    private val _userLists = mutableStateOf<List<UserList>>(emptyList())
    val userLists: State<List<UserList>> = _userLists

    init {
        getUserLists()
    }

    private fun getUserLists() {
        viewModelScope.launch {
            favouriteRepository.getUserLists().collect { lists ->
                _userLists.value = lists
            }
        }
    }
    fun shareMovie() {
        val movie = _state.value.movie ?: return
        val shareText = "Bu filme bayılacaksın! ${movie.title}: https://movieapp.com/movie/${movie.id}"
        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = android.content.Intent.createChooser(intent, "Filmi Paylaş").apply {
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.startActivity(chooser)
    }

    fun addMovieToList(listId: String) {
        if (authRepository.currentUserId == null) {
            viewModelScope.launch {
                _authError.send(application.getString(R.string.movie_auth_error_list))
            }
            return
        }

        val movie = _state.value.movie ?: return
        viewModelScope.launch {
            val movieDto = com.example.movieapp.domain.model.MovieDto(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath,
                overview = movie.overview,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage,
                genreIds = movie.genres.map { it.id }
            )
            addMovieToListUseCase(listId, movieDto)
        }
    }

    fun getMovieDetails(movieId : Int){
        viewModelScope.launch{
            viewModelScope.launch {
                favouriteRepository.isFavourite(movieId).collect { isFav ->
                    _state.value = _state.value.copy(isFavorite = isFav)
                }
            }
            viewModelScope.launch {
                watchedRepository.isWatched(movieId).collect { isWat ->
                    _state.value = _state.value.copy(isWatched = isWat)
                }
            }
            viewModelScope.launch {
                ratingRepository.getUserRating(movieId).collect { rating ->
                    _state.value = _state.value.copy(userRating = rating?.rating ?: 0)
                }
            }
            viewModelScope.launch {
                ratingRepository.getGlobalRating(movieId).collect { globalRating ->
                    _state.value = _state.value.copy(
                        globalRating = globalRating?.average ?: 0.0,
                        ratingCount = (globalRating?.count ?: 0).toLong()
                    )
                }
            }
            val isSameMovie = _state.value.movie?.id == movieId
            _state.value = _state.value.copy(
                isLoading = true,
                watchProviders = null,
                watchProvidersChecked = false,
                error = null,
                isTrailerError = false,
                movie = if (isSameMovie) _state.value.movie else null,
                trailerKey = if (isSameMovie) _state.value.trailerKey else null
            )
            
            val detailsResult = getMovieDetailsUseCase(movieId)
            val trailerResult = getMovieTrailerUseCase(movieId)
            val similarResult = getSimilarMoviesUseCase(movieId)
            val watchProvidersResult = getWatchProvidersUseCase(movieId)
            
            val key = if (trailerResult is RestResult.Success) {
                android.util.Log.d("MovieDetailVM", "Trailer key fetched: ${trailerResult.data}")
                trailerResult.data
            } else {
                android.util.Log.e("MovieDetailVM", "Trailer fetch failed")
                null
            }
            val similarMovies = if (similarResult is RestResult.Success) similarResult.data ?: emptyList() else emptyList()
            val watchProviders = if (watchProvidersResult is RestResult.Success) watchProvidersResult.data else null

            when (detailsResult){
                is RestResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        movie = detailsResult.data,
                        trailerKey = key,
                        similarMovies = similarMovies,
                        watchProviders = watchProviders,
                        watchProvidersChecked = true
                    )
                    
                    // Add to recent
                    detailsResult.data?.let { movie ->
                        val movieDto = com.example.movieapp.domain.model.MovieDto(
                            id = movie.id,
                            title = movie.title,
                            posterPath = movie.posterPath,
                            overview = movie.overview,
                            releaseDate = movie.releaseDate,
                            voteAverage = movie.voteAverage,
                            genreIds = movie.genres.map { it.id }
                        )
                        viewModelScope.launch {
                            addRecentMovieUseCase(movieDto)
                        }
                    }
                }
                is RestResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = detailsResult.message ?: application.getString(R.string.movie_error_unknown),
                        watchProvidersChecked = true
                    )
                }
                else -> Unit
            }
        }
    }
    fun onTrailerError() {
        _state.value = _state.value.copy(isTrailerError = true)
    }

    fun createList(name: String) {
        viewModelScope.launch {
            favouriteRepository.createList(name)
        }
    }
    fun toggleFavorite() {
        if (authRepository.currentUserId == null) {
            viewModelScope.launch {
                _authError.send(application.getString(R.string.movie_auth_error_favorite))
            }
            return
        }

        val movie = _state.value.movie ?: return
        viewModelScope.launch {
            val movieDto = com.example.movieapp.domain.model.MovieDto(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath,
                overview = movie.overview,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage,
                genreIds = movie.genres.map { it.id }
            )

            toggleFavoriteUseCase(movieDto)
        }
    }

    fun toggleWatched() {
        if (authRepository.currentUserId == null) {
            viewModelScope.launch {
                _authError.send(application.getString(R.string.movie_auth_error_favorite))
            }
            return
        }

        val movie = _state.value.movie ?: return
        viewModelScope.launch {
            val movieDto = com.example.movieapp.domain.model.MovieDto(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath,
                overview = movie.overview,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage,
                genreIds = movie.genres.map { it.id }
            )

            if (_state.value.isWatched) {
                watchedRepository.deleteWatched(movieDto)
            } else {
                watchedRepository.insertWatched(movieDto)
            }
        }
    }

    fun rateMovie(rating: Int) {
        if (authRepository.currentUserId == null) {
            viewModelScope.launch {
                _authError.send(application.getString(R.string.movie_auth_error_favorite))
            }
            return
        }

        val movie = _state.value.movie ?: return
        viewModelScope.launch {
            val movieDto = com.example.movieapp.domain.model.MovieDto(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath,
                overview = movie.overview,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage,
                genreIds = movie.genres.map { it.id }
            )
            ratingRepository.submitRating(movieDto, rating)
        }
    }
}
