package com.example.movieapp.feature.movies.domain.usecase

import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.repository.FavouriteRepository
import javax.inject.Inject

class AddMovieToListUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {
    suspend operator fun invoke(listId: String, movie: MovieDto) {
        favouriteRepository.addMovieToList(listId, movie)
    }
}
