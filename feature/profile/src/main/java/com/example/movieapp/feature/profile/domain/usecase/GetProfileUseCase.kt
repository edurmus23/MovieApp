package com.example.movieapp.feature.profile.domain.usecase

import com.example.movieapp.data.local.dao.FavoriteMovieDao
import com.example.movieapp.data.local.dao.UserListDao
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.repository.SearchRepository
import com.example.movieapp.domain.repository.WatchedRepository
import com.example.movieapp.domain.repository.SocialRepository
import com.example.movieapp.feature.rating.domain.repository.RatingRepository
import com.example.movieapp.domain.util.RestResult
import com.example.movieapp.feature.profile.domain.model.ProfileData
import com.example.movieapp.feature.profile.domain.repository.ProfileRepository
import com.example.movieapp.feature.profile.R
import android.content.Context
import android.util.Log
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.data.local.entity.FavoriteMovieEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val favoriteMovieDao: FavoriteMovieDao,
    private val userListDao: UserListDao,
    private val profileRepository: ProfileRepository,
    private val searchRepository: SearchRepository,
    private val watchedRepository: WatchedRepository,
    private val socialRepository: SocialRepository,
    private val ratingRepository: RatingRepository,
    @ApplicationContext private val context: Context
) {
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<ProfileData?> {
        return authRepository.authState.flatMapLatest { userId ->
            if (userId == null) return@flatMapLatest flowOf(null)

            combine(
                favoriteMovieDao.getFavoriteMovies(userId),
                userListDao.getWatchlistMovieCount(userId),
                profileRepository.getRecentMovies(userId),
                authRepository.userName,
                authRepository.currentUsername,
                authRepository.userImageUrl,
                watchedRepository.getWatchedMovieCount(),
                socialRepository.getFollowingCount(userId),
                ratingRepository.getUserRatingsCount(userId),
                ratingRepository.getUserAverageRating(userId)
            ) { flows ->
                val favorites = flows[0] as List<FavoriteMovieEntity>
                val watchlistCount = flows[1] as Int
                val recentMovies = flows[2] as List<MovieDto>
                val reactiveName = flows[3] as String?
                val reactiveUsername = flows[4] as String?
                val reactiveImageUrl = flows[5] as String?
                val watchedCount = flows[6] as Int
                val followingCount = flows[7] as Int
                val ratingsCount = flows[8] as Int
                val averageRating = flows[9] as Double
                
                Log.d("GetProfileUseCase", "Flow updated. ImageURL: $reactiveImageUrl, Name: $reactiveName")

                try {
                    val email = authRepository.currentUserEmail
                    val name = reactiveName ?: authRepository.currentUserName
                    val username = reactiveUsername ?: email?.substringBefore("@") ?: "username"
                    val joinTimestamp = authRepository.currentUserJoinDate
                    val finalImageUrl = reactiveImageUrl?.takeIf { it.isNotBlank() }

                    val joinDateStr = if (joinTimestamp != null) {
                        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                        sdf.format(Date(joinTimestamp))
                    } else context.getString(R.string.profile_default_join_date)

                    // Calculate Top Genres
                    val allGenreIdsStrings: List<String> = favorites.flatMap { entity -> 
                        entity.genreIds?.split(",")?.filter { it.isNotBlank() } ?: emptyList() 
                    }
                    val genreCounts = allGenreIdsStrings.groupingBy { it }.eachCount()
                    val topGenreIds = genreCounts.toList()
                        .sortedByDescending { it.second }
                        .take(4)
                        .mapNotNull { it.first.toIntOrNull() }

                    val genreNames = if (topGenreIds.isNotEmpty()) {
                        when (val result = searchRepository.getGenres()) {
                            is RestResult.Success -> {
                                val allGenres = result.data ?: emptyList()
                                topGenreIds.mapNotNull { id -> allGenres.find { it.id == id }?.name }
                            }
                            else -> emptyList()
                        }
                    } else emptyList()

                    ProfileData(
                        name = name ?: context.getString(R.string.profile_default_user_name),
                        username = "@$username",
                        joinDate = joinDateStr,
                        watchedCount = watchedCount,
                        watchlistCount = watchlistCount,
                        ratingsCount = ratingsCount,
                        favoriteGenres = genreNames,
                        recentlyViewed = recentMovies,
                        moviesThisMonth = watchedCount,
                        averageRating = averageRating,
                        followingCount = followingCount,
                        profilePictureUrl = finalImageUrl
                    )
                } catch (e: Exception) {
                    Log.e("GetProfileUseCase", "Error mapping profile data", e)
                    null
                }
            }
        }
    }
}
