package com.example.movieapp.feature.movies.domain.usecase

import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {
    suspend operator fun invoke(movie: MovieDto) {
        val isFavorite = favouriteRepository.isFavourite(movie.id).first()
        if (isFavorite) {
            favouriteRepository.deleteFavourite(movie)
        } else {
            favouriteRepository.insertFavourite(movie)
        }
    }
}
