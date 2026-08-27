package com.example.movieapp.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface MovieNavKey : NavKey

@Serializable
data object Login : MovieNavKey

@Serializable
data object Register : MovieNavKey

@Serializable
data object Movies : MovieNavKey

@Serializable
data class MovieDetail(val movieId: Int) : MovieNavKey

@Serializable
data object Search : MovieNavKey

@Serializable
data class Favorites(val initialTab: Int = 0) : MovieNavKey

@Serializable
data object Social : MovieNavKey

@Serializable
data class ListDetail(val listId: String, val listName: String, val userId: String) : MovieNavKey

@Serializable
data object MyLists : MovieNavKey

@Serializable
data object Profile : MovieNavKey

@Serializable
data class PublicProfile(val userId: String) : MovieNavKey

@Serializable
data object RecentMovies : MovieNavKey

@Serializable
data object WatchedMovies : MovieNavKey

@Serializable
data object Settings : MovieNavKey

@Serializable
data class RatedMovies(val userId: String) : MovieNavKey

@Serializable
data object AiChat : MovieNavKey
