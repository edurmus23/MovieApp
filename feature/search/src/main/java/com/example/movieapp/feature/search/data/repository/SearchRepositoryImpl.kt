package com.example.movieapp.feature.search.data.repository

import com.example.movieapp.domain.model.GenreDto
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.SearchHistory
import com.example.movieapp.domain.repository.SearchRepository
import com.example.movieapp.domain.util.RestResult
import com.example.movieapp.feature.search.data.local.dao.SearchHistoryDao
import com.example.movieapp.feature.search.data.local.entity.SearchHistoryEntity
import com.example.movieapp.feature.search.data.remote.SearchApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val dao: SearchHistoryDao,
    private val apiService: SearchApiService
) : SearchRepository {
    override fun getSearchHistory(): Flow<List<SearchHistory>> {
        return dao.getSearchHistory().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertSearch(query: String) {
        dao.insertAndCleanUp(SearchHistoryEntity(query = query))
    }

    override suspend fun deleteSearch(query: String) {
        dao.deleteSearch(query)
    }

    override suspend fun clearAllHistory() {
        dao.clearAllHistory()
    }

    override suspend fun searchMovies(query: String): RestResult<List<MovieDto>> {
        return try {
            val response = apiService.searchMovies(query = query, page = 1)
            RestResult.Success(response.results)
        } catch (e: Exception) {
            RestResult.Error(e.localizedMessage ?: "Arama sırasında bir hata oluştu")
        }
    }

    override suspend fun getGenres(): RestResult<List<GenreDto>> {
        return try {
            val response = apiService.getGenres()
            RestResult.Success(response.genres)
        } catch (e: Exception) {
            RestResult.Error(e.localizedMessage ?: "Film türleri yüklenirken hata oluştu")
        }
    }
}
