package com.example.movieapp.feature.movies.domain.usecase

import com.example.movieapp.domain.repository.MoviesRepository
import com.example.movieapp.domain.util.RestResult
import javax.inject.Inject

class GetMovieTrailerUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(movieId: Int): RestResult<String?> {
        return repository.getMovieTrailer(movieId)
    }
}
