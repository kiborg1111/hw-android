package com.example.api.u_i.components

import com.example.api.model.Character

sealed class CharacterUiState {
    object Loading : CharacterUiState()

    data class Success(
        val characters: List<Character>,
        val endReached: Boolean,
        val paginationError: Boolean = false
    ) : CharacterUiState()

    object Empty : CharacterUiState()

    data class Error(
        val message: String
    ) : CharacterUiState()
}