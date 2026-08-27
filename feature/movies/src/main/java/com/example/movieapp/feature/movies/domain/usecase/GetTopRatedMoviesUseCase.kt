package com.example.movieapp.feature.movies.domain.usecase

import androidx.paging.PagingData
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopRatedMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    operator fun invoke(): Flow<PagingData<MovieDto>> {
        return repository.getTopRatedMoviesPaged()
    }
}
