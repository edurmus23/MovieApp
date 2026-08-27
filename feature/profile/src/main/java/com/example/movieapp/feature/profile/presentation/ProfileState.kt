package com.example.movieapp.feature.profile.presentation

import com.example.movieapp.domain.model.MovieDto

data class ProfileState(
    val name: String = "",
    val username: String = "",
    val userId: String = "",
    val joinDate: String = "",
    val watchedCount: Int = 0,
    val watchlistCount: Int = 0,
    val ratingsCount: Int = 0,
    val favoriteGenres: List<String> = emptyList(),
    val recentlyViewed: List<MovieDto> = emptyList(),
    val moviesThisMonth: Int = 0,
    val averageRating: Double = 0.0,
    val followingCount: Int = 0,
    val followingUsers: List<com.example.movieapp.domain.model.User> = emptyList(),
    val showFollowingSheet: Boolean = false,
    val isFollowingLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val profilePictureUrl : String? = null
)
