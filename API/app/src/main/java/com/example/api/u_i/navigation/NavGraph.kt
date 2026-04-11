package com.example.api.u_i.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.api.u_i.screen.list.*
import com.example.api.u_i.screen.detail.*

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(navController, startDestination = "list") {

        composable("list") {

            val vm: ListViewModel = viewModel()

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

        composable("detail/{id}") {

            val vm: DetailViewModel = viewModel()
            val id = it.arguments?.getString("id")?.toInt() ?: 0

            LaunchedEffect(Unit) {
                vm.load(id)
            }

            DetailScreen(
                state = vm.state,
                onRetry = { vm.load(id) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}