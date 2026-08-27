package com.example.movieapp.feature.watched.data.repository

import com.example.movieapp.data.local.dao.WatchedMovieDao
import com.example.movieapp.data.local.entity.WatchedMovieEntity
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.repository.WatchedRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WatchedRepositoryImpl @Inject constructor(
    private val dao: WatchedMovieDao,
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore
) : WatchedRepository {

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var watchedListener: ListenerRegistration? = null

    init {
        repositoryScope.launch {
            authRepository.authState.collectLatest { userId ->
                if (userId != null) {
                    startRealtimeSync(userId)
                } else {
                    stopListeners()
                }
            }
        }
    }

    private fun stopListeners() {
        watchedListener?.remove()
        watchedListener = null
    }

    private fun getWatchedCollection(userId: String) =
        firestore.collection("users").document(userId).collection("watched")

    private fun startRealtimeSync(userId: String) {
        stopListeners()

        watchedListener = getWatchedCollection(userId).addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener

            snapshot?.documentChanges?.forEach { change ->
                val movie = change.document.toObject(MovieDto::class.java)
                when (change.type) {
                    DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                        repositoryScope.launch {
                            dao.insertWatched(
                                WatchedMovieEntity(
                                    id = movie.id,
                                    userId = userId,
                                    title = movie.title,
                                    posterPath = movie.posterPath,
                                    voteAverage = movie.voteAverage,
                                    releaseDate = movie.releaseDate,
                                    genreIds = movie.genreIds.joinToString(",")
                                )
                            )
                        }
                    }
                    DocumentChange.Type.REMOVED -> {
                        repositoryScope.launch {
                            dao.deleteWatchedById(movie.id, userId)
                        }
                    }
                }
            }
        }
    }

    override suspend fun insertWatched(movie: MovieDto) {
        val userId = authRepository.currentUserId ?: return
        try {
            getWatchedCollection(userId).document(movie.id.toString()).set(movie).await()
        } catch (e: Exception) { }

        dao.insertWatched(
            WatchedMovieEntity(
                id = movie.id,
                userId = userId,
                title = movie.title,
                posterPath = movie.posterPath,
                voteAverage = movie.voteAverage,
                releaseDate = movie.releaseDate,
                genreIds = movie.genreIds.joinToString(",")
            )
        )
    }

    override suspend fun deleteWatched(movie: MovieDto) {
        val userId = authRepository.currentUserId ?: return
        try {
            getWatchedCollection(userId).document(movie.id.toString()).delete().await()
        } catch (e: Exception) {}

        dao.deleteWatched(
            WatchedMovieEntity(
                id = movie.id,
                userId = userId,
                title = movie.title,
                posterPath = movie.posterPath,
                voteAverage = movie.voteAverage,
                releaseDate = movie.releaseDate,
                genreIds = movie.genreIds.joinToString(",")
            )
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getWatchedMovies(): Flow<List<MovieDto>> {
        return authRepository.authState.flatMapLatest { userId ->
            if (userId == null) return@flatMapLatest flowOf(emptyList())
            dao.getWatchedMovies(userId).map { entities ->
                entities.map { it.toMovieDto() }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun isWatched(movieId: Int): Flow<Boolean> {
        return authRepository.authState.flatMapLatest { userId ->
            if (userId == null) return@flatMapLatest flowOf(false)
            dao.isWatched(movieId, userId)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getWatchedMovieCount(): Flow<Int> {
        return authRepository.authState.flatMapLatest { userId ->
            if (userId == null) return@flatMapLatest flowOf(0)
            dao.getWatchedMovieCount(userId)
        }
    }

    override suspend fun syncFromRemote() {
        authRepository.currentUserId?.let { startRealtimeSync(it) }
    }
}
