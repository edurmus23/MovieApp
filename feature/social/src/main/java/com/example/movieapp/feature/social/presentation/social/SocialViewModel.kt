package com.example.movieapp.feature.social.presentation.social

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.User
import com.example.movieapp.domain.repository.SocialRepository
import com.example.movieapp.domain.util.RestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(
    private val socialRepository: SocialRepository
) : ViewModel() {

    private val _state = mutableStateOf(SocialState())
    val state: State<SocialState> = _state

    private var searchJob: Job? = null

    init {
        // İlk açılışta bazı kullanıcıları getir
        onSearchQueryChanged("")
    }

    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isNotBlank()) {
                delay(500)
            }
            _state.value = _state.value.copy(isLoading = true)
            
            when (val result = socialRepository.searchUsers(query)) {
                is RestResult.Success -> {
                    _state.value = _state.value.copy(
                        searchResults = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is RestResult.Error -> {
                    _state.value = _state.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
                else -> {
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
    }
}
