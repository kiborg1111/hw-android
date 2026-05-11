package com.example.api.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.api.model.Character
import com.example.api.u_i.components.CharacterDetailUiState
import com.example.api.u_i.screens.CharacterDetailScreen
import org.junit.Rule
import org.junit.Test

class CharacterDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `shows character detail`() {

        composeTestRule.setContent {
            CharacterDetailScreen(
                uiState = CharacterDetailUiState.Success(
                    Character(
                        1,
                        "Rick",
                        "Alive",
                        "Human",
                        ""
                    )
                ),
                onRetry = {},
                onBack = {},
                onFavouriteClick = {}
            )
        }

        composeTestRule.onNodeWithText("Rick")
            .assertExists()

        composeTestRule.onNodeWithText("Alive")
            .assertExists()
    }
}