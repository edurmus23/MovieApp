package com.example.movieapp.ai.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.ChatMessage
import com.example.movieapp.domain.model.ChatRole
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.repository.ChatRepository
import com.example.movieapp.domain.repository.SearchRepository
import com.example.movieapp.domain.util.RestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun sendMessage(content: String) {
        if (content.isBlank()) return

        val userMessage = ChatMessage(ChatRole.USER, content)
        _messages.add(userMessage)
        
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = repository.sendMessage(content)
                val reply = response.reply ?: ""
                android.util.Log.d("AiChatVM", "Gelen Cevap: $reply")
                
                val extractedTitles = extractMovieTitles(reply)
                android.util.Log.d("AiChatVM", "Çıkarılan Başlıklar: $extractedTitles")

                val searchedMovies = extractedTitles.map { title ->
                    async {
                        when (val result = searchRepository.searchMovies(title)) {
                            is RestResult.Success -> {
                                val movie = result.data?.firstOrNull()
                                android.util.Log.d("AiChatVM", "Arama Sonucu ($title): ${movie?.title ?: "Bulunamadı"}")
                                movie
                            }
                            is RestResult.Error -> {
                                android.util.Log.e("AiChatVM", "Arama Hatası ($title): ${result.message}")
                                null
                            }
                            else -> null
                        }
                    }
                }.awaitAll().filterNotNull()

                val finalMovies = (response.movies ?: emptyList()) + searchedMovies
                android.util.Log.d("AiChatVM", "Toplam Film Sayısı: ${finalMovies.size}")

                val aiMessage = ChatMessage(
                    role = ChatRole.AI,
                    content = reply,
                    movies = finalMovies.takeIf { it.isNotEmpty() }?.distinctBy { it.id }
                )
                _messages.add(aiMessage)
            } catch (e: Exception) {
                android.util.Log.e("AiChatVM", "Full connection error", e)
                _error.value = "Hata: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun extractMovieTitles(content: String): List<String> {
        // Handles various bullet types and captures title + year
        val regex = Regex("""(?m)^\s*[-*•–—]\s*(.*?)\s*\((\d{4})\)""")
        return regex.findAll(content).map { 
            it.groupValues[1].trim().removeSuffix(":").trim() 
        }.toList()
    }
}
