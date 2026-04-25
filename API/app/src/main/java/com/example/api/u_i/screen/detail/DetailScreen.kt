package com.example.api.u_i.screen.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.api.domain.model.Post
import com.example.api.u_i.state.UiState

@Composable
fun DetailScreen(
    state: UiState<Post>,
    isFavorite: Boolean,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TextButton(onClick = onBack) {
            Text("Назад")
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (state) {

            UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Column {
                    Text(state.message)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) {
                        Text("Повторить")
                    }
                }
            }

            is UiState.Success -> {

                val post = state.data

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {

                    Column(Modifier.padding(16.dp)) {

                        Text(post.title)
                        Spacer(Modifier.height(8.dp))
                        Text(post.description)

                        Spacer(Modifier.height(16.dp))

                        Button(onClick = onToggleFavorite) {
                            Text(
                                if (isFavorite)
                                    "Убрать из избранного"
                                else
                                    "В избранное"
                            )
                        }
                    }
                }
            }

            else -> {
                Text("Нет данных")
            }
        }
    }
}