package com.example.movieapp.feature.profile.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.util.RestResult
import com.example.movieapp.feature.profile.domain.usecase.UpdatePasswordUseCase
import com.example.movieapp.feature.profile.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    init {
        _state.value = _state.value.copy(currentName = authRepository.currentUserName ?: "")
    }

    fun updateProfile(name: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, isSuccess = false)
            when (val result = updateProfileUseCase(name)) {
                is RestResult.Success -> {
                    _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                }
                is RestResult.Error -> {
                    _state.value = _state.value.copy(isLoading = false, error = result.message)
                }
                else -> {}
            }
        }
    }

    fun updatePassword(password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, isSuccess = false)
            when (val result = updatePasswordUseCase(password)) {
                is RestResult.Success -> {
                    _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                }
                is RestResult.Error -> {
                    _state.value = _state.value.copy(isLoading = false, error = result.message)
                }
                else -> {}
            }
        }
    }

    fun resetState() {
        _state.value = _state.value.copy(isSuccess = false, error = null)
    }
}
