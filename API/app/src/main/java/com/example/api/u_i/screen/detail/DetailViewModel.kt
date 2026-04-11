package com.example.api.u_i.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.data.repository.PostRepository
import com.example.api.domain.model.Post
import com.example.api.u_i.state.UiState
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class DetailViewModel : ViewModel() {

    private val repository = PostRepository()

    var state by mutableStateOf<UiState<Post>>(UiState.Loading)
        private set

    fun load(id: Int) {
        viewModelScope.launch {
            state = UiState.Loading
            try {
                val data = repository.getPost(id)
                state = UiState.Success(data)
            } catch (e: Exception) {
                state = UiState.Error("Ошибка")
            }
        }
    }
}