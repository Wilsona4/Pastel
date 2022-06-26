package com.example.pastel.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pastel.data.remote.model.Article
import com.example.pastel.domain.repository.NewsRepository
import com.example.pastel.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class NewsUiState(
    val articles: List<Article> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    // UI state exposed to the UI
    var uiState by mutableStateOf(NewsUiState(loading = true))
        private set

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getNewsArticles()
    }

    /*Get Articles List*/
    private fun getNewsArticles() {
        uiState = uiState.copy(loading = true)

        viewModelScope.launch {
            newsRepository.getNews().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        uiState = uiState.copy(
                            loading = false,
                            articles = result.data ?: emptyList(),
                        )
                    }
                    is Resource.Error -> {
                        uiState = uiState.copy(
                            loading = false,
                            articles = result.data ?: emptyList(),
                        )

                        _uiEvent.send(result.message ?: "Something went wrong")

                    }
                    is Resource.Loading -> {
                        uiState = uiState.copy(loading = true)
                    }
                }
            }
        }
    }
}