package com.example.api.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.api.model.Character
import com.example.api.u_i.components.CharacterUiState
import com.example.api.u_i.screens.CharacterListScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

    @Test
    fun `error state calls retry`() {

        var retryCalled = false

        composeTestRule.setContent {
            CharacterListScreen(
                uiState = CharacterUiState.Error("Error"),
                searchQuery = "",
                onSearchChange = {},
                onRetry = { retryCalled = true },
                onClick = {},
                onLoadMore = {},
                onFavouriteClick = {},
                favourites = emptyList()
            )
        }

        composeTestRule
            .onNodeWithText("Retry")
            .performClick()

        assertTrue(retryCalled)
    }

    @Test
    fun `empty state shows no results`() {

        composeTestRule.setContent {
            CharacterListScreen(
                uiState = CharacterUiState.Empty,
                searchQuery = "",
                onSearchChange = {},
                onRetry = {},
                onClick = {},
                onLoadMore = {},
                onFavouriteClick = {},
                favourites = emptyList()
            )
        }

        composeTestRule
            .onNodeWithText("No results")
            .assertExists()
    }

    @Test
    fun `click character passes correct id`() {

        var clickedId: Int? = null

        composeTestRule.setContent {
            CharacterListScreen(
                uiState = CharacterUiState.Success(
                    listOf(
                        Character(7, "Rick", "Alive", "Human", "", false)
                    ),
                    endReached = true,
                    paginationError = false
                ),
                searchQuery = "",
                onSearchChange = {},
                onRetry = {},
                onClick = { clickedId = it },
                onLoadMore = {},
                onFavouriteClick = {},
                favourites = emptyList()
            )
        }

        composeTestRule
            .onNodeWithText("Rick")
            .performClick()

        assertEquals(7, clickedId)
    }
}