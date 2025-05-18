package com.example.todo_list.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo_list.data.repository.TodoRepository
import com.example.todo_list.ui.detail.DetailScreen
import com.example.todo_list.ui.detail.DetailViewModel
import com.example.todo_list.ui.list.ListScreen
import com.example.todo_list.ui.list.ListViewModel

sealed class Screen(val route: String) {
    data object List : Screen("list")
    data object Detail : Screen("detail/{todoId}") {
        fun createRoute(todoId: Int) = "detail/$todoId"
    }
}

@Composable
fun TodoNavGraph(
    repository: TodoRepository,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.List.route
    ) {
        composable(Screen.List.route) {
            val viewModel = remember { ListViewModel(repository) }
            ListScreen(
                viewModel = viewModel,
                onTodoClick = { todoId ->
                    navController.navigate(Screen.Detail.createRoute(todoId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("todoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId") ?: return@composable
            val viewModel = remember { DetailViewModel(repository, todoId) }
            DetailScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}