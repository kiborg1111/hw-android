package com.example.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class Status(val title: String) {
    PLANNED("Хочу смотреть"),
    WATCHING("Смотрю"),
    DONE("Просмотрено")
}

data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val genre: String,
    val status: Status
)

data class UiState(
    val search: String = "",
    val filter: Status? = null,
    val movies: List<Movie> = emptyList()
)

class MoviesStateHolder {

    var state by mutableStateOf(
        UiState(
            movies = listOf(
                Movie(1, "Интерстеллар", 2014, "Фантастика", Status.PLANNED),
                Movie(2, "Начало", 2010, "Триллер", Status.WATCHING),
                Movie(3, "Матрица", 1999, "Фантастика", Status.DONE),
                Movie(4, "Аватар", 2009, "Приключения", Status.PLANNED)
            )
        )
    )
        private set

    fun onSearchChange(value: String) {
        state = state.copy(search = value)
    }

    fun onFilterChange(status: Status?) {
        state = state.copy(filter = status)
    }

    fun onNextStatus(movie: Movie) {
        val newStatus = when (movie.status) {
            Status.PLANNED -> Status.WATCHING
            Status.WATCHING -> Status.DONE
            Status.DONE -> Status.PLANNED
        }

        state = state.copy(
            movies = state.movies.map {
                if (it.id == movie.id) it.copy(status = newStatus) else it
            }
        )
    }

    fun getFilteredMovies(): List<Movie> {
        return state.movies
            .filter {
                it.title.contains(state.search, ignoreCase = true)
            }
            .filter {
                state.filter == null || it.status == state.filter
            }
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val holder = remember { MoviesStateHolder() }
            val state = holder.state
            val movies = holder.getFilteredMovies()

            Column(Modifier.padding(16.dp)) {

                Text("Поиск фильмов", style = MaterialTheme.typography.headlineMedium)

                Spacer(Modifier.height(10.dp))

                TextField(
                    value = state.search,
                    onValueChange = { holder.onSearchChange(it) },
                    label = { Text("Поиск") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Button(onClick = { holder.onFilterChange(null) }) {
                        Text("Все")
                    }

                    Button(onClick = { holder.onFilterChange(Status.PLANNED) }) {
                        Text("Хочу")
                    }

                    Button(onClick = { holder.onFilterChange(Status.WATCHING) }) {
                        Text("Смотрю")
                    }

                    Button(onClick = { holder.onFilterChange(Status.DONE) }) {
                        Text("Готово")
                    }
                }

                Spacer(Modifier.height(10.dp))

                if (movies.isEmpty()) {
                    Text("Ничего не найдено")
                }

                LazyColumn {

                    items(movies) { movie ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {

                            Column(Modifier.padding(12.dp)) {

                                Text(movie.title)
                                Text("Год: ${movie.year}")
                                Text("Жанр: ${movie.genre}")
                                Text("Статус: ${movie.status.title}")

                                Spacer(Modifier.height(8.dp))

                                Button(onClick = {
                                    holder.onNextStatus(movie)
                                }) {
                                    Text("Статус")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}