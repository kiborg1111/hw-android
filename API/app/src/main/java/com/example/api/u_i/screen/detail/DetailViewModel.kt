package com.example.api.u_i.screen.detail

import kotlinx.coroutines.flow.first
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.data.repository.PostRepository
import com.example.api.data.repository.FavoriteRepository
import com.example.api.data.local.FavoritePost
import com.example.api.domain.model.Post
import com.example.api.u_i.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    var state by mutableStateOf<UiState<Post>>(UiState.Loading)
        private set

    var isFavorite by mutableStateOf(false)
        private set

    private var currentPost: Post? = null

    fun load(id: Int) {
        viewModelScope.launch {
            state = UiState.Loading

            try {
                val post = postRepository.getPost(id)
                currentPost = post
                state = UiState.Success(post)

                val exists = favoriteRepository
                    .getFavorites()
                    .first()
                    .any { it.id == id }

                isFavorite = exists

            } catch (e: Exception) {
                state = UiState.Error("Ошибка загрузки")
            }
        }
    }

    fun toggleFavorite() {
        val post = currentPost ?: return

        viewModelScope.launch {
            val fav = FavoritePost(
                id = post.id,
                title = post.title,
                description = post.description
            )

            val exists = favoriteRepository
                .getFavorites()
                .first()
                .any { it.id == post.id }

            if (exists) {
                favoriteRepository.remove(fav)
                isFavorite = false
            } else {
                favoriteRepository.add(fav)
                isFavorite = true
            }
        }
    }
}