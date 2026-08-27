package com.example.movieapp.feature.rating.data.repository

import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.feature.rating.domain.model.GlobalMovieRating
import com.example.movieapp.feature.rating.domain.model.UserRating
import com.example.movieapp.feature.rating.domain.repository.RatingRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RatingRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : RatingRepository {

    override suspend fun submitRating(movie: MovieDto, rating: Int) {
        val userId = authRepository.currentUserId ?: return
        val userRatingRef = firestore.collection("users").document(userId)
            .collection("ratings").document(movie.id.toString())
        val globalRatingRef = firestore.collection("global_ratings").document(movie.id.toString())

        firestore.runTransaction { transaction ->
            val userRatingDoc = transaction.get(userRatingRef)
            val oldRating = userRatingDoc.getLong("rating")?.toInt()

            val globalDoc = transaction.get(globalRatingRef)
            var count = globalDoc.getLong("count") ?: 0L
            var sum = globalDoc.getDouble("sum") ?: 0.0

            if (oldRating == null) {
                count += 1
                sum += rating
            } else {
                sum = sum - oldRating + rating
            }
            val average = if (count > 0) sum / count else 0.0

            val userRating = UserRating(
                movieId = movie.id,
                rating = rating,
                timestamp = System.currentTimeMillis(),
                movieTitle = movie.title,
                moviePosterPath = movie.posterPath,
                movieVoteAverage = movie.voteAverage
            )

            transaction.set(userRatingRef, userRating)
            transaction.set(globalRatingRef, mapOf(
                "movieId" to movie.id,
                "count" to count,
                "sum" to sum,
                "average" to average
            ))
        }.await()
    }

    override fun getUserRating(movieId: Int): Flow<UserRating?> = callbackFlow {
        val userId = authRepository.currentUserId
        if (userId == null) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val listener = firestore.collection("users").document(userId)
            .collection("ratings").document(movieId.toString())
            .addSnapshotListener { snapshot, _ ->
                trySend(snapshot?.toObject(UserRating::class.java))
            }
        awaitClose { listener.remove() }
    }

    override fun getGlobalRating(movieId: Int): Flow<GlobalMovieRating?> = callbackFlow {
        val listener = firestore.collection("global_ratings").document(movieId.toString())
            .addSnapshotListener { snapshot, _ ->
                trySend(snapshot?.toObject(GlobalMovieRating::class.java))
            }
        awaitClose { listener.remove() }
    }

    override fun getUserRatingsCount(userId: String): Flow<Int> = callbackFlow {
        val listener = firestore.collection("users").document(userId).collection("ratings")
            .addSnapshotListener { snapshot, _ ->
                trySend(snapshot?.size() ?: 0)
            }
        awaitClose { listener.remove() }
    }

    override fun getUserAverageRating(userId: String): Flow<Double> = callbackFlow {
        val listener = firestore.collection("users").document(userId).collection("ratings")
            .addSnapshotListener { snapshot, _ ->
                val ratings = snapshot?.documents?.mapNotNull { it.getLong("rating") } ?: emptyList()
                val average = if (ratings.isNotEmpty()) ratings.average() else 0.0
                trySend(average)
            }
        awaitClose { listener.remove() }
    }

    override fun getRatedMovies(userId: String): Flow<List<UserRating>> = callbackFlow {
        val listener = firestore.collection("users").document(userId)
            .collection("ratings")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                val ratings = snapshot?.documents?.mapNotNull { it.toObject(UserRating::class.java) } ?: emptyList()
                trySend(ratings)
            }
        awaitClose { listener.remove() }
    }
}
