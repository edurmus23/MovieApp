package com.example.movieapp.feature.social.data.repository

import android.util.Log
import com.example.movieapp.domain.model.User
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.repository.SocialRepository
import com.example.movieapp.domain.util.RestResult
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import java.util.Locale

class SocialRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : SocialRepository {

    override suspend fun searchUsers(query: String): RestResult<List<User>> {
        return try {
            val cleanQuery = query.trim().removePrefix("@").trim()
            val usersCollection = firestore.collection("users")

            // DEBUG: Arama kutusu boşsa rastgele 5 kişi göster (Altyapı testi için)
            if (cleanQuery.isEmpty()) {
                val debugSnapshot = usersCollection.limit(5).get().await()
                val debugUsers = debugSnapshot.documents.mapNotNull { mapDocToUser(it) }
                return RestResult.Success(debugUsers)
            }
            
            val lowercaseQuery = cleanQuery.lowercase(Locale.ROOT)
            Log.d("SocialSearch", "--- Arama: $lowercaseQuery ---")

            // 1. Sorgu: username_lowercase
            val q1 = usersCollection
                .whereGreaterThanOrEqualTo("username_lowercase", lowercaseQuery)
                .whereLessThanOrEqualTo("username_lowercase", lowercaseQuery + "\uf8ff")
                .limit(20)
                .get()

            // 2. Sorgu: email (Tam eşleşme)
            val q2 = usersCollection
                .whereEqualTo("email", cleanQuery)
                .limit(5)
                .get()

            val s1 = q1.await()
            val s2 = q2.await()

            Log.d("SocialSearch", "S1 (username) boyutu: ${s1.size()}")
            Log.d("SocialSearch", "S2 (email) boyutu: ${s2.size()}")

            var allDocs = (s1.documents + s2.documents).distinctBy { it.id }

            // 3. EĞER HİÇBİR ŞEY BULUNAMADIYSA: Manuel Filtreleme (Son Çare)
            if (allDocs.isEmpty()) {
                Log.d("SocialSearch", "Sorgularda bulunamadı, 50 kayıt çekilip manuel aranıyor...")
                val fallbackSnapshot = usersCollection.limit(50).get().await()
                allDocs = fallbackSnapshot.documents.filter { doc ->
                    val username = doc.getString("username")?.lowercase(Locale.ROOT) ?: ""
                    val name = doc.getString("name")?.lowercase(Locale.ROOT) ?: ""
                    val email = doc.getString("email")?.lowercase(Locale.ROOT) ?: ""
                    
                    username.contains(lowercaseQuery) || 
                    name.contains(lowercaseQuery) || 
                    email.contains(lowercaseQuery)
                }
            }

            val finalUsers = allDocs.mapNotNull { mapDocToUser(it) }
                .filter { it.id != authRepository.currentUserId } // Kendini gösterme

            Log.d("SocialSearch", "Sonuç: ${finalUsers.size} kullanıcı bulundu")
            RestResult.Success(finalUsers)
        } catch (e: Exception) {
            Log.e("SocialSearch", "Sorgu Hatası", e)
            RestResult.Error(e.localizedMessage ?: "Arama başarısız")
        }
    }

    private fun mapDocToUser(doc: com.google.firebase.firestore.DocumentSnapshot): User? {
        val username = doc.getString("username") ?: doc.getString("email")?.substringBefore("@") ?: "isimsiz"
        return User(
            id = doc.id,
            name = doc.getString("name") ?: "Kullanıcı",
            email = doc.getString("email") ?: "",
            username = username,
            profilePictureUrl = doc.getString("profilePictureUrl")
        )
    }

    override suspend fun followUser(targetUserId: String): RestResult<Unit> {
        val currentUserId = authRepository.currentUserId ?: return RestResult.Error("Giriş yapmalısınız")
        return try {
            //
            val batch = firestore.batch()
            val myFollowingRef = firestore.collection("users").document(currentUserId).collection("following").document(targetUserId)
            batch.set(myFollowingRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
            val targetFollowersRef = firestore.collection("users").document(targetUserId).collection("followers").document(currentUserId)
            batch.set(targetFollowersRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
            batch.commit().await()
            RestResult.Success(Unit)
        } catch (e: Exception) {
            RestResult.Error("Takip edilemedi")
        }
    }

    override suspend fun unfollowUser(targetUserId: String): RestResult<Unit> {
        val currentUserId = authRepository.currentUserId ?: return RestResult.Error("Giriş yapmalısınız")
        return try {
            val batch = firestore.batch()
            batch.delete(firestore.collection("users").document(currentUserId).collection("following").document(targetUserId))
            batch.delete(firestore.collection("users").document(targetUserId).collection("followers").document(currentUserId))
            batch.commit().await()
            RestResult.Success(Unit)
        } catch (e: Exception) {
            RestResult.Error("Takip bırakılamadı")
        }
    }

    override fun isFollowing(targetUserId: String): Flow<Boolean> = callbackFlow {
        val currentUserId = authRepository.currentUserId
        if (currentUserId == null) { trySend(false); return@callbackFlow }
        val listener = firestore.collection("users").document(currentUserId).collection("following").document(targetUserId)
            .addSnapshotListener { snapshot, _ -> trySend(snapshot?.exists() ?: false) }
        awaitClose { listener.remove() }
    }

    override fun getFollowersCount(userId: String): Flow<Int> = callbackFlow {
        val listener = firestore.collection("users").document(userId).collection("followers")
            .addSnapshotListener { snapshot, _ -> trySend(snapshot?.size() ?: 0) }
        awaitClose { listener.remove() }
    }

    override fun getFollowingCount(userId: String): Flow<Int> = callbackFlow {
        val listener = firestore.collection("users").document(userId).collection("following")
            .addSnapshotListener { snapshot, _ -> trySend(snapshot?.size() ?: 0) }
        awaitClose { listener.remove() }
    }

    override fun getFollowingUsers(userId: String): Flow<List<User>> = callbackFlow {
        val listener = firestore.collection("users").document(userId).collection("following")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val followingIds = snapshot?.documents?.map { it.id } ?: emptyList()
                if (followingIds.isEmpty()) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                // Get details of followed users
                firestore.collection("users")
                    .whereIn(com.google.firebase.firestore.FieldPath.documentId(), followingIds.take(30))
                    .addSnapshotListener { usersSnapshot, usersError ->
                        if (usersError != null) {
                            trySend(emptyList())
                            return@addSnapshotListener
                        }
                        val users = usersSnapshot?.documents?.mapNotNull { mapDocToUser(it) } ?: emptyList()
                        trySend(users)
                    }
            }
        awaitClose { listener.remove() }
    }
}
