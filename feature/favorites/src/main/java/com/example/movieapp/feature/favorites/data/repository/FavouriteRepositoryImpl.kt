package com.example.movieapp.feature.favorites.data.repository

import android.util.Log
import com.example.movieapp.data.local.dao.FavoriteMovieDao
import com.example.movieapp.data.local.dao.UserListDao
import com.example.movieapp.data.local.entity.FavoriteMovieEntity
import com.example.movieapp.data.local.entity.ListMovieCrossRef
import com.example.movieapp.data.local.entity.UserListEntity
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.UserList
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.repository.FavouriteRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val dao : FavoriteMovieDao,
    private val userListDao: UserListDao,
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore,
) : FavouriteRepository {

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var favoritesListener: ListenerRegistration? = null
    private var listsListener: ListenerRegistration? = null
    private val listMoviesListeners = ConcurrentHashMap<String, ListenerRegistration>()

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
        favoritesListener?.remove()
        listsListener?.remove()
        favoritesListener = null
        listsListener = null
        
        listMoviesListeners.values.forEach { it.remove() }
        listMoviesListeners.clear()
    }

    private fun getFavoritesCollection(userId: String) = 
        firestore.collection("users").document(userId).collection("favorites")

    private fun getListsCollection(userId: String) = 
        firestore.collection("users").document(userId).collection("lists")

    private fun startRealtimeSync(userId: String) {
        stopListeners()
        
        favoritesListener = getFavoritesCollection(userId).addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            
            snapshot?.documentChanges?.forEach { change ->
                val movie = change.document.toObject(MovieDto::class.java)
                when (change.type) {
                    DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                        repositoryScope.launch {
                            dao.insertFavorite(
                                FavoriteMovieEntity(
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
                            dao.deleteFavoriteById(movie.id, userId)
                        }
                    }
                }
            }
        }

        listsListener = getListsCollection(userId).addSnapshotListener { snapshot, _ ->
            snapshot?.documentChanges?.forEach { change ->
                val listId = change.document.id
                when (change.type) {
                    DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                        val name = change.document.getString("name") ?: ""
                        val createdAt = change.document.getLong("createdAt") ?: System.currentTimeMillis()
                        
                        repositoryScope.launch {
                            userListDao.insertList(UserListEntity(listId, name, userId, createdAt))
                            setupListMoviesListener(userId, listId)
                        }
                    }
                    DocumentChange.Type.REMOVED -> {
                        repositoryScope.launch {
                            userListDao.deleteListWithMovies(listId)
                            listMoviesListeners[listId]?.remove()
                            listMoviesListeners.remove(listId)
                        }
                    }
                }
            }
        }
    }

    private fun setupListMoviesListener(userId: String, listId: String) {
        listMoviesListeners[listId]?.remove()
        
        val listener = getListsCollection(userId).document(listId).collection("movies")
            .addSnapshotListener { moviesSnapshot, _ ->
                moviesSnapshot?.documentChanges?.forEach { movieChange ->
                    val movie = movieChange.document.toObject(MovieDto::class.java)
                    when (movieChange.type) {
                        DocumentChange.Type.ADDED -> {
                            repositoryScope.launch {
                                userListDao.insertMovieToList(ListMovieCrossRef(listId, movie.id))
                            }
                        }
                        DocumentChange.Type.REMOVED -> {
                            repositoryScope.launch {
                                userListDao.removeMovieFromList(listId, movie.id)
                            }
                        }
                        DocumentChange.Type.MODIFIED -> { }
                    }
                }
            }
        
        listMoviesListeners[listId] = listener
    }

    override suspend fun insertFavourite(movie: MovieDto) {
        val userId = authRepository.currentUserId ?: return
        try {
            getFavoritesCollection(userId).document(movie.id.toString()).set(movie).await()
        } catch (e: Exception) { }
        
        dao.insertFavorite(
            FavoriteMovieEntity(
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

    override suspend fun deleteFavourite(movie: MovieDto) {
        val userId = authRepository.currentUserId ?: return
        try {
            getFavoritesCollection(userId).document(movie.id.toString()).delete().await()
        } catch (e: Exception) {}

        dao.deleteFavorite(
            FavoriteMovieEntity(
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
    override fun getFavouriteMovies(): Flow<List<MovieDto>> {
        return authRepository.authState.flatMapLatest { userId ->
            if (userId == null) return@flatMapLatest flowOf(emptyList())
            dao.getFavoriteMovies(userId).map { entities ->
                entities.map { it.toMovieDto() }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun isFavourite(movieId: Int): Flow<Boolean> {
        return authRepository.authState.flatMapLatest { userId ->
            if (userId == null) return@flatMapLatest flowOf(value = false)
            dao.isFavorite(movieId, userId)
        }
    }

    override suspend fun createList(name: String) {
        val userId = authRepository.currentUserId ?: return
        val listId = UUID.randomUUID().toString()
        val listEntity = UserListEntity(id = listId, name = name, userId = userId)
        try {
            getListsCollection(userId).document(listId).set(listEntity).await()
        } catch (e: Exception) {}
        userListDao.insertList(listEntity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserLists(): Flow<List<UserList>> {
        return authRepository.authState.flatMapLatest { userId ->
            if (userId == null) return@flatMapLatest flowOf(emptyList())
            userListDao.getUserListsWithMetadata(userId).map { entities ->
                entities.map { it.toUserList() }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTotalSavedMovieCount(): Flow<Int> {
        return authRepository.authState.flatMapLatest { userId ->
            if (userId == null) return@flatMapLatest flowOf(0)
            userListDao.getWatchlistMovieCount(userId)
        }
    }

    override suspend fun addMovieToList(listId: String, movie: MovieDto) {
        val userId = authRepository.currentUserId ?: return
        try {
            getListsCollection(userId).document(listId).collection("movies")
                .document(movie.id.toString()).set(movie).await()
        } catch (e: Exception) {}
        insertFavourite(movie)
        userListDao.insertMovieToList(ListMovieCrossRef(listId, movie.id))
    }

    override fun getMoviesInList(userId: String, listId: String): Flow<List<MovieDto>> = callbackFlow {
        val targetUserId = if (userId.isBlank()) authRepository.currentUserId else userId
        
        if (targetUserId == null) {
            trySend(emptyList())
            return@callbackFlow
        }

        val collection = if (listId == "favorites") {
            getFavoritesCollection(targetUserId)
        } else {
            getListsCollection(targetUserId).document(listId).collection("movies")
        }

        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            val movies = snapshot?.documents?.mapNotNull { it.toObject(MovieDto::class.java) } ?: emptyList()
            trySend(movies)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun removeMovieFromList(listId: String, movieId: Int) {
        val userId = authRepository.currentUserId ?: return
        try {
            if (listId == "favorites") {
                getFavoritesCollection(userId).document(movieId.toString()).delete().await()
                dao.deleteFavoriteById(movieId, userId)
            } else {
                getListsCollection(userId).document(listId).collection("movies")
                    .document(movieId.toString()).delete().await()
                userListDao.removeMovieFromList(listId, movieId)
            }
        } catch (e: Exception) { }
    }

    override suspend fun deleteList(listId: String) {
        val userId = authRepository.currentUserId ?: return
        try {
            getListsCollection(userId).document(listId).delete().await()
        } catch (e: Exception) {}
        userListDao.deleteList(UserListEntity(id = listId, name = "", userId = userId))
    }

    override suspend fun syncFromRemote() {
        authRepository.currentUserId?.let { startRealtimeSync(it) }
    }
}
