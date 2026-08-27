package com.example.movieapp.feature.profile.presentation.public_profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.feature.profile.domain.usecase.GetPublicProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicProfileViewModel @Inject constructor(
    private val getPublicProfileUseCase: GetPublicProfileUseCase,
    private val socialRepository: com.example.movieapp.domain.repository.SocialRepository
) : ViewModel() {

    private val _state = mutableStateOf(PublicProfileState())
    val state: State<PublicProfileState> = _state

    private val _isFollowing = mutableStateOf(false)
    val isFollowing: State<Boolean> = _isFollowing

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            // Listen to following status
            viewModelScope.launch {
                socialRepository.isFollowing(userId).collectLatest {
                    _isFollowing.value = it
                }
            }

            getPublicProfileUseCase(userId).collectLatest { data ->
                if (data != null) {
                    _state.value = _state.value.copy(
                        profileData = data,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Profil bulunamadı"
                    )
                }
            }
        }
    }

    fun toggleFollow(userId: String) {
        viewModelScope.launch {
            if (_isFollowing.value) {
                socialRepository.unfollowUser(userId)
            } else {
                socialRepository.followUser(userId)
            }
        }
    }
}
