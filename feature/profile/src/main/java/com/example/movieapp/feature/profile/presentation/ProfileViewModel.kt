package com.example.movieapp.feature.profile.presentation

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.feature.profile.domain.usecase.GetProfileUseCase
import com.example.movieapp.feature.profile.domain.usecase.LogoutUseCase
import com.example.movieapp.feature.profile.domain.usecase.UpdateProfileUseCase
import com.example.movieapp.feature.profile.domain.usecase.UploadProfilePictureUseCase
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.repository.SocialRepository
import com.example.movieapp.domain.util.RestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val authRepository: AuthRepository,
    private val socialRepository: SocialRepository,
    private val application: Application
) : ViewModel() {

    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    private var followingListJob: Job? = null

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getProfileUseCase().collectLatest { profileData ->
                if (profileData != null) {
                    _state.value = _state.value.copy(
                        name = profileData.name,
                        username = profileData.username,
                        userId = authRepository.currentUserId ?: "",
                        joinDate = profileData.joinDate,
                        watchedCount = profileData.watchedCount,
                        watchlistCount = profileData.watchlistCount,
                        ratingsCount = profileData.ratingsCount,
                        favoriteGenres = profileData.favoriteGenres,
                        recentlyViewed = profileData.recentlyViewed,
                        moviesThisMonth = profileData.moviesThisMonth,
                        averageRating = profileData.averageRating,
                        followingCount = profileData.followingCount,
                        profilePictureUrl = profileData.profilePictureUrl,
                        isLoggedIn = true,
                        isLoading = false
                    )
                } else {
                    _state.value = ProfileState(isLoggedIn = false, isLoading = false)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    fun uploadProfilePicture(uri: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = uploadProfilePictureUseCase(uri)) {
                is RestResult.Error -> {
                    _state.value = _state.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
                is RestResult.Success -> {
                    _state.value = _state.value.copy(isLoading = false)
                }
                else -> {
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            updateProfileUseCase(newName)
        }
    }

    fun shareProfile() {
        val userId = authRepository.currentUserId ?: return
        val shareText = "MovieApp profilime göz at! https://movieapp.com/profile/$userId"
        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = android.content.Intent.createChooser(intent, "Profili Paylaş").apply {
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.startActivity(chooser)
    }

    fun showFollowingList() {
        val userId = authRepository.currentUserId ?: return
        _state.value = _state.value.copy(showFollowingSheet = true, isFollowingLoading = true)
        
        followingListJob?.cancel()
        followingListJob = viewModelScope.launch {
            socialRepository.getFollowingUsers(userId).collectLatest { users ->
                _state.value = _state.value.copy(
                    followingUsers = users,
                    isFollowingLoading = false
                )
            }
        }
    }

    fun hideFollowingList() {
        _state.value = _state.value.copy(showFollowingSheet = false)
        followingListJob?.cancel()
        followingListJob = null
    }
}
