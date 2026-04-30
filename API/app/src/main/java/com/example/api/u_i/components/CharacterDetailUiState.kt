package com.example.api.u_i.components

import com.example.api.model.Character

sealed class CharacterDetailUiState {

    object Loading : CharacterDetailUiState()

    data class Success(
        val character: Character
    ) : CharacterDetailUiState()

    data class Error(
        val message: String
    ) : CharacterDetailUiState()
}