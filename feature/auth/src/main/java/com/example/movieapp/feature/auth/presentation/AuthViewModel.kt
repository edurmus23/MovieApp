package com.example.movieapp.feature.auth.presentation

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.domain.util.RestResult
import com.example.movieapp.feature.auth.domain.usecase.PasswordValidationResult
import com.example.movieapp.feature.auth.domain.usecase.ValidatePasswordUseCase
import com.example.movieapp.feature.auth.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val application: Application
) : ViewModel() {
    private val _state = mutableStateOf(AuthState())
    val state: State<AuthState> = _state

    val currentUserEmail: String?
        get() = repository.currentUserId // Actually this is ID, but Firebase user has email.
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            val result = repository.login(email.trim(), password)
            when (result) {
                is RestResult.Success -> {
                    _state.value = AuthState(isSuccess = true)
                }
                is RestResult.Error -> {
                    _state.value = AuthState(error = result.message)
                }
                else -> {
                    _state.value = AuthState(isLoading = false)
                }
            }
        }
    }

    fun register(fullName: String, username: String, email: String, psw: String) {
        val trimmedEmail = email.trim() // 1. E-postayı temizle

        // 2. Şifreyi kontrol et
        val validationResult = validatePasswordUseCase(psw)

        if (validationResult is PasswordValidationResult.Failure) {
            // Hata varsa işlemi durdur ve ekrana hatayı bas
            val errorMsg = when(validationResult.errorKey) {
                "auth_error_password_too_short" -> application.getString(R.string.auth_error_password_too_short)
                "auth_error_password_no_uppercase" -> application.getString(R.string.auth_error_password_no_uppercase)
                "auth_error_password_no_digit" -> application.getString(R.string.auth_error_password_no_digit)
                else -> validationResult.errorKey
            }
            _state.value = AuthState(error = errorMsg)
            return
        }

        // 3. Her şey tamamsa devam et...
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            val result = repository.register(fullName, username, trimmedEmail, psw)
            when (result) {
                is RestResult.Success -> {
                    _state.value = AuthState(isSuccess = true)
                }
                is RestResult.Error -> {
                    _state.value = AuthState(error = result.message)
                }
                else -> {
                    _state.value = AuthState(isLoading = false)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _state.value = AuthState(isSuccess = true)
        }
    }

    fun resetState() {
        _state.value = AuthState()
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            val result = repository.signInWithGoogle(idToken)
            when (result) {
                is RestResult.Success -> {
                    _state.value = AuthState(isSuccess = true)
                }
                is RestResult.Error -> {
                    _state.value = AuthState(error = result.message)
                }
                else -> {
                    _state.value = AuthState(isLoading = false)
                }
            }
        }
    }
}
