package com.example.api.u_i.screen.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.api.domain.model.Post
import com.example.api.u_i.state.UiState

@Composable
fun ListScreen(
    state: UiState<List<Post>>,
    search: String,
    onSearchChange: (String) -> Unit,
    onRetry: () -> Unit,
    onClick: (Int) -> Unit
) {

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Posts",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        // 🔍 ПОИСК
        TextField(
            value = search,
            onValueChange = onSearchChange,
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        when (state) {

            UiState.Loading -> Text("Loading...")

            is UiState.Error -> Column {
                Text(state.message)
                Button(onClick = onRetry) { Text("Retry") }
            }

            UiState.Empty -> Text("Nothing found")

            is UiState.Success -> {

                LazyColumn {
                    items(state.data) { post ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable { onClick(post.id) }
                        ) {

                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                Text(
                                    text = post.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    text = post.description,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}