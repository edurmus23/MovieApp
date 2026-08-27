package com.example.movieapp.feature.profile.data.repository

import com.example.movieapp.data.local.dao.RecentMovieDao
import com.example.movieapp.data.local.entity.RecentMovieEntity
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.UserList
import com.example.movieapp.domain.repository.SearchRepository
import com.example.movieapp.feature.profile.domain.model.ProfileData
import com.example.movieapp.feature.profile.domain.repository.ProfileRepository
import com.example.movieapp.domain.util.RestResult
import com.example.movieapp.feature.rating.domain.repository.RatingRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val recentMovieDao: RecentMovieDao,
    private val firestore: FirebaseFirestore,
    private val searchRepository: SearchRepository,
    private val ratingRepository: RatingRepository
) : ProfileRepository {

    override fun getRecentMovies(userId: String): Flow<List<MovieDto>> = callbackFlow {
        val listener = firestore.collection("users").document(userId).collection("recent_movies")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                
                val movies = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        val entity = doc.toObject(RecentMovieEntity::class.java)
                        entity?.toMovieDto()
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()
                
                trySend(movies)
            }
        
        awaitClose { listener.remove() }
    }

    override suspend fun addRecentMovie(userId: String, movie: MovieDto) {
        val recentEntity = RecentMovieEntity(
            id = movie.id,
            userId = userId,
            title = movie.title,
            posterPath = movie.posterPath,
            voteAverage = movie.voteAverage,
            releaseDate = movie.releaseDate,
            timestamp = System.currentTimeMillis()
        )

        recentMovieDao.insertRecentMovie(recentEntity)

        try {
            val collection = firestore.collection("users").document(userId).collection("recent_movies")
            collection.document(movie.id.toString()).set(recentEntity).await()

            val snapshot = collection.orderBy("timestamp", Query.Direction.DESCENDING).get().await()
            if (snapshot.size() > 10) {
                snapshot.documents.drop(10).forEach { doc ->
                    doc.reference.delete().await()
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("ProfileRepo", "Error syncing recent movies", e)
        }
    }

    override fun getPublicProfile(userId: String): Flow<ProfileData?> {
        val genresFlow = flow {
            emit(searchRepository.getGenres())
        }

        return combine(
            getUserInfoFlow(userId),
            getCollectionFlow(userId, "favorites"),
            getCollectionFlow(userId, "watched"),
            getUserListsFlow(userId),
            getRecentMovies(userId),
            genresFlow,
            ratingRepository.getUserRatingsCount(userId),
            ratingRepository.getUserAverageRating(userId)
        ) { args ->
            val userInfo = args[0] as? UserMetadata ?: return@combine null
            val favorites = args[1] as List<MovieDto>
            val watched = args[2] as List<MovieDto>
            val lists = args[3] as List<UserList>
            val recent = args[4] as List<MovieDto>
            val genresResult = args[5] as RestResult<List<com.example.movieapp.domain.model.GenreDto>>
            val ratingsCount = args[6] as Int
            val averageRating = args[7] as Double

            val genreMap = if (genresResult is RestResult.Success) {
                genresResult.data?.associate { it.id to it.name } ?: emptyMap()
            } else {
                emptyMap()
            }

            ProfileData(
                name = userInfo.name,
                username = "@${userInfo.username}",
                joinDate = userInfo.joinDate,
                watchedCount = watched.size,
                watchlistCount = lists.sumOf { it.movieCount },
                ratingsCount = ratingsCount,
                favoriteGenres = calculateTopGenres(favorites, genreMap),
                recentlyViewed = recent,
                moviesThisMonth = watched.size, // Simplified
                averageRating = averageRating,
                profilePictureUrl = userInfo.profilePictureUrl,
                userLists = lists,
                followingCount = 0
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getUserListsFlow(userId: String): Flow<List<UserList>> = callbackFlow<List<DocumentSnapshot>> {
        val listener = firestore.collection("users").document(userId).collection("lists")
            .addSnapshotListener { snapshot, _ ->
                trySend(snapshot?.documents ?: emptyList())
            }
        awaitClose { listener.remove() }
    }.flatMapLatest { documents ->
        if (documents.isEmpty()) return@flatMapLatest flowOf(emptyList())
        
        val listFlows = documents.map { doc ->
            val listId = doc.id
            val name = doc.getString("name") ?: "Adsız Liste"
            
            callbackFlow<Int> {
                val listener = firestore.collection("users").document(userId)
                    .collection("lists").document(listId).collection("movies")
                    .addSnapshotListener { snapshot, _ ->
                        trySend(snapshot?.size() ?: 0)
                    }
                awaitClose { listener.remove() }
            }.map { count ->
                UserList(listId, name, count, userId)
            }
        }
        
        combine(listFlows) { it.toList() }
    }

    private data class UserMetadata(
        val name: String,
        val username: String,
        val joinDate: String,
        val profilePictureUrl: String?
    )

    private fun getUserInfoFlow(userId: String): Flow<UserMetadata?> = callbackFlow {
        val listener = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null || !snapshot.exists()) {
                    trySend(null)
                    return@addSnapshotListener
                }

                val name = snapshot.getString("name") ?: "Kullanıcı"
                val email = snapshot.getString("email") ?: ""
                val username = snapshot.getString("username")?.takeIf { it.isNotBlank() } 
                    ?: email.substringBefore("@").takeIf { it.isNotBlank() } 
                    ?: name.lowercase(Locale.ROOT).replace(" ", "")
                val profilePictureUrl = snapshot.getString("profilePictureUrl")
                val createdAt = snapshot.getLong("createdAt") ?: System.currentTimeMillis()

                val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                val joinDateStr = sdf.format(Date(createdAt))

                trySend(UserMetadata(name, username, joinDateStr, profilePictureUrl))
            }
        awaitClose { listener.remove() }
    }

    private fun getCollectionFlow(userId: String, collectionName: String): Flow<List<MovieDto>> = callbackFlow {
        val listener = firestore.collection("users").document(userId).collection(collectionName)
            .addSnapshotListener { snapshot, _ ->
                val movies = snapshot?.documents?.mapNotNull { it.toObject(MovieDto::class.java) } ?: emptyList()
                trySend(movies)
            }
        awaitClose { listener.remove() }
    }

    private fun calculateTopGenres(movies: List<MovieDto>, genreMap: Map<Int, String>): List<String> {
        return movies.flatMap { it.genreIds }
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }
            .take(3)
            .map { pair -> genreMap[pair.first] ?: pair.first.toString() }
    }
}
