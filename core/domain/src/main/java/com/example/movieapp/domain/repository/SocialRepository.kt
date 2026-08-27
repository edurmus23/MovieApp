package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.User
import com.example.movieapp.domain.util.RestResult
import kotlinx.coroutines.flow.Flow

interface SocialRepository {
    // Kullanıcı arama
    suspend fun searchUsers(query: String): RestResult<List<User>>

    // Takip etme/bırakma
    suspend fun followUser(targetUserId: String): RestResult<Unit>
    suspend fun unfollowUser(targetUserId: String): RestResult<Unit>

    // Takip durumu kontrolü
    fun isFollowing(targetUserId: String): Flow<Boolean>

    // Sayaçlar
    fun getFollowersCount(userId: String): Flow<Int>
    fun getFollowingCount(userId: String): Flow<Int>

    // Listeler
    fun getFollowingUsers(userId: String): Flow<List<User>>
}