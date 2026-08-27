package com.example.movieapp.feature.movies.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.MovieResponseDto

class MoviePagingSource(
    private val fetchMovies: suspend (Int) -> MovieResponseDto
) : PagingSource<Int, MovieDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        val page = params.key ?: 1
        Log.d("MoviePagingSource", "Loading page: $page")

        return try {
            val response = fetchMovies(page)
            val movies = response.results
            Log.d("MoviePagingSource", "Response success: ${movies.size} movies found")
            
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("MoviePagingSource", "Response error: ${e.localizedMessage}", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
