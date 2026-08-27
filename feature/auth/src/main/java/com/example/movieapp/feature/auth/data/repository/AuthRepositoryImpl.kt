package com.example.movieapp.feature.auth.data.repository

import android.util.Log
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.repository.util.AppDatabase
import com.example.movieapp.domain.util.RestResult
import com.example.movieapp.feature.auth.data.local.util.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import java.util.Locale

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sessionManager: SessionManager,
    private val database: AppDatabase,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : AuthRepository {

    override val authState: Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.uid)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override val userName: Flow<String?> = sessionManager.userName
    override val currentUsername: Flow<String?> = sessionManager.userUsername
    
    override val userImageUrl: Flow<String?> = sessionManager.userImageUrl.map { localUrl ->
        localUrl ?: firebaseAuth.currentUser?.photoUrl?.toString()
    }

    override suspend fun uploadProfilePicture(uri: String): RestResult<String> {
        return try {
            val userId = currentUserId ?: throw Exception("Kullanıcı bulunamadı")
            val storageRef = storage.reference.child("profile_pictures/users/$userId.jpg")
            val fileUri = android.net.Uri.parse(uri)
            storageRef.putFile(fileUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(android.net.Uri.parse(downloadUrl))
                .build()
            firebaseAuth.currentUser?.updateProfile(profileUpdates)?.await()
            firebaseAuth.currentUser?.reload()?.await()

            updateUserSearchKeys(userId, currentUserName ?: "", currentUsernameValue = null) // This will fetch current from firestore

            firestore.collection("users").document(userId)
                .set(mapOf("profilePictureUrl" to downloadUrl), SetOptions.merge())
                .await()

            sessionManager.saveSession(
                id = userId,
                name = currentUserName ?: "",
                email = currentUserEmail ?: "",
                imageUrl = downloadUrl
            )

            RestResult.Success(downloadUrl)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Upload failed", e)
            RestResult.Error(e.localizedMessage ?: "Fotoğraf yüklenemedi")
        }
    }

    private suspend fun updateUserSearchKeys(userId: String, name: String, currentUsernameValue: String?) {
        try {
            val doc = firestore.collection("users").document(userId).get().await()
            val finalUsername = currentUsernameValue ?: doc.getString("username") ?: firebaseAuth.currentUser?.email?.substringBefore("@") ?: "isimsiz"
            
            val updateData = mapOf(
                "name" to name,
                "name_lowercase" to name.lowercase(Locale.ROOT),
                "username" to finalUsername,
                "username_lowercase" to finalUsername.lowercase(Locale.ROOT)
            )
            firestore.collection("users").document(userId).set(updateData, SetOptions.merge()).await()
        } catch (e: Exception) {
            Log.e("AuthRepository", "Search keys update failed", e)
        }
    }

    override suspend fun login(email: String, password: String): RestResult<String> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("Kullanıcı ID bulunamadı")
            user.reload().await()
            
            val userId = user.uid
            val name = user.displayName ?: email.substringBefore("@")
            
            val userDoc = firestore.collection("users").document(userId).get().await()
            val remoteImageUrl = userDoc.getString("profilePictureUrl") ?: user.photoUrl?.toString()
            val currentUsernameValue = userDoc.getString("username") ?: email.substringBefore("@")
            
            updateUserSearchKeys(userId, name, currentUsernameValue)
            
            sessionManager.saveSession(userId, name, email, imageUrl = remoteImageUrl, username = currentUsernameValue)
            
            RestResult.Success(userId)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login error: ${e.message}", e)
            RestResult.Error(e.localizedMessage ?: "Giriş sırasında hata oluştu")
        }
    }

    override suspend fun register(fullName: String, username: String, email: String, password: String): RestResult<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("Kayıt sırasında hata oluştu")
            
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build()
            user.updateProfile(profileUpdates).await()
            user.reload().await()
            
            val userId = user.uid
            
            val userData = mapOf(
                "uid" to userId,
                "name" to fullName,
                "name_lowercase" to fullName.lowercase(Locale.ROOT),
                "username" to username,
                "username_lowercase" to username.lowercase(Locale.ROOT),
                "email" to email,
                "createdAt" to (user.metadata?.creationTimestamp ?: System.currentTimeMillis())
            )
            firestore.collection("users").document(userId).set(userData).await()
            
            sessionManager.saveSession(userId, fullName, email, username)
            RestResult.Success(userId)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Register error: ${e.message}", e)
            RestResult.Error(e.localizedMessage ?: "Kayıt sırasında hata oluştu")
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
        sessionManager.clearSession()
        withContext(Dispatchers.IO) {
            database.clearAllData()
        }
    }

    override val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    override val currentUserEmail: String?
        get() = firebaseAuth.currentUser?.email

    override val currentUserName: String?
        get() = firebaseAuth.currentUser?.displayName ?: firebaseAuth.currentUser?.email?.substringBefore("@")

    override val currentUserJoinDate: Long?
        get() = firebaseAuth.currentUser?.metadata?.creationTimestamp

    override suspend fun updateProfile(name: String): RestResult<Unit> {
        return try {
            val user = firebaseAuth.currentUser ?: throw Exception("Kullanıcı bulunamadı")
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(profileUpdates).await()
            user.reload().await()
            
            val userId = user.uid
            updateUserSearchKeys(userId, name, null)
            
            sessionManager.saveSession(userId, name, user.email ?: "")
            
            RestResult.Success(Unit)
        } catch (e: Exception) {
            RestResult.Error(e.localizedMessage ?: "Profil güncellenemedi")
        }
    }

    override suspend fun updatePassword(password: String): RestResult<Unit> {
        return try {
            val user = firebaseAuth.currentUser ?: throw Exception("Kullanıcı bulunamadı")
            user.updatePassword(password).await()
            RestResult.Success(Unit)
        } catch (e: Exception) {
            RestResult.Error(e.localizedMessage ?: "Şifre güncellenemedi. Lütfen tekrar giriş yapıp deneyin.")
        }
    }

    override suspend fun signInWithGoogle(idToken: String): RestResult<String> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val user = result.user ?: throw Exception("Google ile giriş yapılamadı")

            val userId = user.uid
            val name = user.displayName ?: ""
            val email = user.email ?: ""
            val photoUrl = user.photoUrl?.toString()
            val username = email.substringBefore("@")

            updateUserSearchKeys(userId, name, username)

            val userData = mutableMapOf(
                "uid" to userId,
                "name" to name,
                "email" to email,
                "lastLogin" to System.currentTimeMillis()
            )
            photoUrl?.let { userData["profilePictureUrl"] = it }

            firestore.collection("users").document(userId).set(userData, SetOptions.merge()).await()

            sessionManager.saveSession(userId, name, email, imageUrl = photoUrl, username = username)

            RestResult.Success(userId)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Google Sign-In Error", e)
            RestResult.Error(e.localizedMessage ?: "Google ile giriş sırasında hata oluştu")
        }
    }
    override suspend fun updateFcmToken(token: String): RestResult<Unit> {
        val userId = currentUserId ?: return RestResult.Error("Kullanıcı bulunamadı")
        return try {
            firestore.collection("users").document(userId)
                .set(mapOf("fcmToken" to token), SetOptions.merge())
                .await()
            RestResult.Success(Unit)
        } catch (e: Exception) {
            RestResult.Error(e.localizedMessage ?: "Token güncellenemedi")
        }
    }
}
