package com.example.api.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.api.model.Character
import com.example.api.u_i.components.CharacterUiState
import com.example.api.u_i.screens.CharacterListScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `shows success state with characters`() {
        val successState = CharacterUiState.Success(
            characters = listOf(Character(1, "Rick", "Alive", "Human", "", false)),
            endReached = true,
            paginationError = false
        )

        composeTestRule.setContent {
            CharacterListScreen(
                uiState = successState,
                searchQuery = "",
                onSearchChange = {},
                onRetry = {},
                onClick = {},
                onLoadMore = {},
                onFavouriteClick = {},
                favourites = emptyList()
            )
        }

        composeTestRule.onNodeWithText("Rick").assertExists()
    }
}