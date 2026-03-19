package com.example.cooking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument


enum class Status(val title: String) {
    WANT("Хочу"),
    COOKING("Готовлю"),
    DONE("Готово")
}

data class Recipe(
    val id: Int,
    val title: String,
    val category: String,
    val time: String,
    val difficulty: String,
    val description: String,
    val status: Status
)


data class UiState(
    val search: String = "",
    val filter: Status? = null,
    val recipes: List<Recipe> = emptyList()
)


class RecipesViewModel : ViewModel() {

    var state by mutableStateOf(
        UiState(
            recipes = listOf(
                Recipe(1, "Карбонара", "Макароны", "30 мин", "Легко", "Яйца, Мука, Соль, Бекон, Сыр", Status.WANT),
                Recipe(2, "Борщ", "Суп", "60 мин", "Средне", "Вода, Свекла, Морковь, Лук, Говядина", Status.COOKING),
                Recipe(3, "Цезарь", "Салат", "15 мин", "Легко", "Салат, Помидор, Курица, Хлеб", Status.DONE),
                Recipe(4, "Пицца 4 сыра", "Выпечка", "40 мин", "Средне", "Мука, Яйцо, Масло, Томатная паста, Чеддер, Моцарелла, Горгонзола, Пармезан", Status.WANT)
            )
        )
    )
        private set

    fun onSearchChange(text: String) {
        state = state.copy(search = text)
    }

    fun onFilterChange(status: Status?) {
        state = state.copy(filter = status)
    }

    fun changeStatus(recipeId: Int) {
        state = state.copy(
            recipes = state.recipes.map {
                if (it.id == recipeId) {
                    val newStatus = when (it.status) {
                        Status.WANT -> Status.COOKING
                        Status.COOKING -> Status.DONE
                        Status.DONE -> Status.WANT
                    }
                    it.copy(status = newStatus)
                } else it
            }
        )
    }

    fun getFiltered(): List<Recipe> {
        return state.recipes
            .filter { it.title.contains(state.search, ignoreCase = true) }
            .filter { state.filter == null || it.status == state.filter }
    }

    fun getById(id: Int): Recipe? {
        return state.recipes.find { it.id == id }
    }


    fun getStats(): Triple<Int, Int, Int> {
        val want = state.recipes.count { it.status == Status.WANT }
        val cooking = state.recipes.count { it.status == Status.COOKING }
        val done = state.recipes.count { it.status == Status.DONE }
        return Triple(want, cooking, done)
    }
}


@Composable
fun ListScreen(viewModel: RecipesViewModel, onOpenDetails: (Int) -> Unit) {

    val state = viewModel.state
    val list = viewModel.getFiltered()
    val (want, cooking, done) = viewModel.getStats()

    Column(Modifier.padding(16.dp)) {

        Text("Рецепты", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(8.dp))


        TextField(
            value = state.search,
            onValueChange = viewModel::onSearchChange,
            label = { Text("Поиск") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))


        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Button(onClick = { viewModel.onFilterChange(null) }) { Text("Все") }
            Button(onClick = { viewModel.onFilterChange(Status.WANT) }) { Text("Хочу") }
            Button(onClick = { viewModel.onFilterChange(Status.COOKING) }) { Text("Готовлю") }
            Button(onClick = { viewModel.onFilterChange(Status.DONE) }) { Text("Готово") }
        }

        Spacer(Modifier.height(8.dp))


        Text("Хочу: $want  Готовлю: $cooking  Готово: $done")

        Spacer(Modifier.height(8.dp))

        if (list.isEmpty()) {
            Text("Ничего не найдено")
        }

        LazyColumn {
            items(list) { recipe ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onOpenDetails(recipe.id) }
                ) {

                    Column(Modifier.padding(12.dp)) {

                        Text(recipe.title)
                        Text("Категория: ${recipe.category}")
                        Text("Время: ${recipe.time}")
                        Text("Сложность: ${recipe.difficulty}")
                        Text("Статус: ${recipe.status.title}")

                        Spacer(Modifier.height(8.dp))

                        Button(onClick = {
                            viewModel.changeStatus(recipe.id)
                        }) {
                            Text("Статус")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DetailsScreen(viewModel: RecipesViewModel, recipeId: Int, onBack: () -> Unit) {

    val recipe = viewModel.getById(recipeId) ?: return

    Column(Modifier.padding(16.dp)) {

        Text(recipe.title, style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(8.dp))

        Text("Категория: ${recipe.category}")
        Text("Время: ${recipe.time}")
        Text("Сложность: ${recipe.difficulty}")
        Text("Ингредиенты: ${recipe.description}")
        Text("Статус: ${recipe.status.title}")

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            viewModel.changeStatus(recipe.id)
        }) {
            Text("Статус")
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}


@Composable
fun App(viewModel: RecipesViewModel) {

    val navController = rememberNavController()

    NavHost(navController, startDestination = "list") {

        composable("list") {
            ListScreen(viewModel) { id ->
                navController.navigate("details/$id")
            }
        }

        composable(
            "details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id") ?: 0

            DetailsScreen(
                viewModel = viewModel,
                recipeId = id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = remember { RecipesViewModel() }
            App(viewModel)
        }
    }
}