package com.example.api.u_i.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.data.repository.PostRepository
import com.example.api.domain.model.Post
import com.example.api.u_i.state.UiState
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class ListViewModel : ViewModel() {

    private val repository = PostRepository()

    var state by mutableStateOf<UiState<List<Post>>>(UiState.Loading)
        private set

    var searchQuery by mutableStateOf("")
        private set

    private var allPosts: List<Post> = emptyList()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            state = UiState.Loading
            try {
                val data = repository.getPosts()
                allPosts = data
                applyFilter()
            } catch (e: Exception) {
                state = UiState.Error("Ошибка загрузки")
            }
        }
    }

    fun onSearchChange(query: String) {
        searchQuery = query
        applyFilter()
    }

    private fun applyFilter() {
        val filtered = allPosts.filter {
            it.title.contains(searchQuery, ignoreCase = true)
        }

        state = if (filtered.isEmpty()) UiState.Empty
        else UiState.Success(filtered)
    }
}