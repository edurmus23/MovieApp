package com.example.movieapp.feature.rating.presentation

import androidx.lifecycle.ViewModel
import com.example.movieapp.feature.rating.domain.repository.RatingRepository

class RatedMoviesViewModel(
    private val ratingRepository: RatingRepository
) : ViewModel() {
    fun getRatedMovies(userId: String) = ratingRepository.getRatedMovies(userId)
}
