package com.example.api.u_i.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.data.local.FavoritePost
import com.example.api.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repo: FavoriteRepository
) : ViewModel() {

    val favorites = repo.getFavorites()

    fun add(post: FavoritePost) {
        viewModelScope.launch {
            repo.add(post)
        }
    }

    fun remove(post: FavoritePost) {
        viewModelScope.launch {
            repo.remove(post)
        }
    }
}