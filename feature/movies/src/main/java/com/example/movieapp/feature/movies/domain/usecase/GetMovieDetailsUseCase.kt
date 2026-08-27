package com.example.movieapp.feature.movies.domain.usecase

import com.example.movieapp.domain.model.MovieDetailDto
import com.example.movieapp.domain.repository.MoviesRepository
import com.example.movieapp.domain.util.RestResult
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(movieId: Int): RestResult<MovieDetailDto> {
        return repository.getMovieDetails(movieId)
    }
}


