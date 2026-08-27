package com.example.movieapp.feature.movies.domain.usecase

import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.repository.MoviesRepository
import com.example.movieapp.domain.util.RestResult
import javax.inject.Inject

class GetBannerMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(): RestResult<List<MovieDto>> {
        return repository.getBannerMovies()
    }
}
