package com.example.api.u_i.screen.list

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.data.repository.PostRepository
import com.example.api.data.repository.FavoriteRepository
import com.example.api.domain.model.Post
import com.example.api.u_i.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    var state by mutableStateOf<UiState<List<Post>>>(UiState.Loading)
        private set

    var searchQuery by mutableStateOf("")
        private set

    private var allPosts = emptyList<Post>()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            state = UiState.Loading
            try {
                allPosts = repository.getPosts()
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

        state = if (filtered.isEmpty())
            UiState.Empty
        else
            UiState.Success(filtered)
    }
}