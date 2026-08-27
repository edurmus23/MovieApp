package com.example.movieapp.feature.movies.domain.usecase

import com.example.movieapp.domain.model.WatchCountryProviderDto
import com.example.movieapp.domain.repository.MoviesRepository
import com.example.movieapp.domain.util.RestResult
import javax.inject.Inject

class GetWatchProvidersUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(movieId: Int): RestResult<WatchCountryProviderDto?> {
        return repository.getWatchProviders(movieId)
    }
}
