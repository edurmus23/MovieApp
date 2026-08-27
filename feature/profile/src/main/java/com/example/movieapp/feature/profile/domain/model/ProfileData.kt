package com.example.movieapp.feature.profile.domain.model

import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.UserList

data class ProfileData(
    val name: String,
    val username: String,
    val joinDate: String,
    val watchedCount: Int,
    val watchlistCount: Int,
    val ratingsCount: Int,
    val favoriteGenres: List<String>,
    val recentlyViewed: List<MovieDto>,
    val moviesThisMonth: Int,
    val averageRating: Double,
    val followingCount: Int = 0,
    val profilePictureUrl : String? = null,
    val userLists: List<UserList> = emptyList()
)
