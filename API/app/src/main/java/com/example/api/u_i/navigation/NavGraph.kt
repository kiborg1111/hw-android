package com.example.api.u_i.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.example.api.u_i.screen.list.ListViewModel
import com.example.api.u_i.screen.detail.DetailViewModel
import com.example.api.u_i.state.UiState
import com.example.api.u_i.screen.list.ListScreen
import com.example.api.u_i.screen.detail.DetailScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {

        composable("list") {

            val vm: ListViewModel = hiltViewModel()

            ListScreen(
                state = vm.state,
                search = vm.searchQuery,
                onSearchChange = vm::onSearchChange,
                onRetry = { vm.load() },
                onClick = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }

        composable("detail/{id}") { backStackEntry ->

            val vm: DetailViewModel = hiltViewModel()

            val id = backStackEntry.arguments
                ?.getString("id")
                ?.toInt()
                ?: 0

            val state = vm.state
            val isFavorite = vm.isFavorite

            LaunchedEffect(id) {
                vm.load(id)
            }

            DetailScreen(
                state = state,
                isFavorite = isFavorite,
                onRetry = { vm.load(id) },
                onBack = { navController.popBackStack() },
                onToggleFavorite = {
                    vm.toggleFavorite()
                }
            )
        }
    }
}