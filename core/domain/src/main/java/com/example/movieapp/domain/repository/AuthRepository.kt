package com.example.movieapp.domain.repository

import com.example.movieapp.domain.util.RestResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): RestResult<String>
    suspend fun register(fullName: String, username: String, email: String, password: String): RestResult<String>
    suspend fun logout()
    val currentUserId: String?
    val currentUserEmail: String?
    val currentUserName: String?
    val currentUserJoinDate: Long?
    val authState: Flow<String?>
    val userName: Flow<String?>
    val currentUsername: Flow<String?>
    suspend fun updateProfile(name: String): RestResult<Unit>
    suspend fun updatePassword(password: String): RestResult<Unit>

    suspend fun signInWithGoogle(idToken: String): RestResult<String>
    suspend fun uploadProfilePicture(uri: String): RestResult<String>
    val userImageUrl: Flow<String?>

    suspend fun updateFcmToken(token: String): RestResult<Unit>
}
