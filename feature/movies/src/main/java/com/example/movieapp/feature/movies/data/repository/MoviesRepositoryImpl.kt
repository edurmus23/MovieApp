package com.example.movieapp.feature.movies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapp.domain.model.MovieDetailDto
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.WatchCountryProviderDto
import com.example.movieapp.domain.model.hasProviders
import com.example.movieapp.domain.repository.MoviesRepository
import com.example.movieapp.domain.util.RestResult
import com.example.movieapp.feature.movies.data.paging.MoviePagingSource
import com.example.movieapp.feature.movies.data.remote.MoviesApiService
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val apiService: MoviesApiService
) : MoviesRepository {

    override fun getPopularMovies(): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource { page -> apiService.getPopularMovies(page) }
            }
        ).flow
    }

    override fun getTopRatedMoviesPaged(): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource { page -> apiService.getTopRatedMovies(page) }
            }
        ).flow
    }

    override fun getUpcomingMoviesPaged(): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource { page -> apiService.getUpcomingMovies(page) }
            }
        ).flow
    }

    override suspend fun getBannerMovies(): RestResult<List<MovieDto>> {
        return try {
            val response = apiService.getPopularMovies(page = 1)
            RestResult.Success(response.results.take(4))
        } catch (e: Exception) {
            RestResult.Error(e.localizedMessage ?: "Banner filmleri yüklenirken hata oluştu")
        }
    }

    override suspend fun getMovieDetails(movieId: Int): RestResult<MovieDetailDto> {
        return try {
            val response = apiService.getMovieDetails(movieId)
            RestResult.Success(response)
        } catch (e: Exception) {
            RestResult.Error(e.localizedMessage ?: "Bilinmeyen bir hata oluştu")
        }
    }

    override suspend fun getMovieTrailer(movieId: Int): RestResult<String?> {
        return try {
            val response = apiService.getMovieVideos(movieId)
            
            // Priority: Official Trailer > Any Trailer > Official Teaser > Any Teaser > Any YouTube Video
            val trailer = response.results.find { 
                it.site == "YouTube" && it.type == "Trailer" && it.isOfficial 
            } ?: response.results.find { 
                it.site == "YouTube" && it.type == "Trailer" 
            } ?: response.results.find {
                it.site == "YouTube" && it.type == "Teaser" && it.isOfficial
            } ?: response.results.find {
                it.site == "YouTube" && it.type == "Teaser"
            } ?: response.results.find {
                it.site == "YouTube"
            }

            RestResult.Success(trailer?.key)
        } catch (e: Exception) {
            RestResult.Error(e.localizedMessage ?: "Fragman yüklenirken hata oluştu")
        }
    }

    override suspend fun getWatchProviders(movieId: Int): RestResult<WatchCountryProviderDto?> {
        return try {
            val response = apiService.getWatchProviders(movieId)
            val countryCode = Locale.getDefault().country.uppercase(Locale.ROOT)
            val countryResult = response.results[countryCode]
                ?: response.results["US"]
                ?: response.results.values.firstOrNull { it.hasProviders() }
            RestResult.Success(countryResult?.takeIf { it.hasProviders() })
        } catch (e: Exception) {
            RestResult.Error(e.localizedMessage ?: "İzleme platformları yüklenirken hata oluştu")
        }
    }

    override suspend fun getTopRatedMovies(): RestResult<List<MovieDto>> {
        return try {
            val response = apiService.getTopRatedMovies(page = 1)
            RestResult.Success(response.results)
        } catch (e: Exception) {
            RestResult.Error(e.localizedMessage ?: "En çok oy alan filmler yüklenirken hata oluştu")
        }
    }

    override suspend fun getUpcomingMovies(): RestResult<List<MovieDto>> {
        return try {
            val response = apiService.getUpcomingMovies(page = 1)
            RestResult.Success(response.results)
        } catch (e: Exception) {
            RestResult.Error(e.localizedMessage ?: "Upcoming filmler yüklenirken hata oluştu")
        }
    }

    override suspend fun getSimilarMovies(movieId: Int): RestResult<List<MovieDto>> {
        return try {
            val response = apiService.getSimilarMovies(movieId = movieId, page = 1)
            RestResult.Success(response.results)
        } catch (e: Exception) {
            RestResult.Error(e.localizedMessage ?: "Benzer filmler yüklenirken hata oluştu")
        }
    }
}
