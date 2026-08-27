package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.UserList
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    suspend fun insertFavourite(movie : MovieDto)
    suspend fun deleteFavourite( movie : MovieDto)
    fun getFavouriteMovies(): Flow<List<MovieDto>>
    fun isFavourite(movieId:Int) : Flow<Boolean>

    // List Management
    suspend fun createList(name: String)
    fun getUserLists(): Flow<List<UserList>>
    fun getTotalSavedMovieCount(): Flow<Int>
    suspend fun addMovieToList(listId: String, movie: MovieDto)
    fun getMoviesInList(userId: String, listId: String): Flow<List<MovieDto>>
    suspend fun removeMovieFromList(listId: String, movieId: Int)
    suspend fun deleteList(listId: String)

    suspend fun syncFromRemote()
}
