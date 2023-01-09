package com.fernanortega.notitask.ui.navigation

import androidx.compose.runtime.*
import com.fernanortega.notitask.viewmodel.LoginViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fernanortega.notitask.ui.login.LoginScreen
import com.fernanortega.notitask.ui.tasks.TaskScreen
import com.fernanortega.notitask.viewmodel.TasksViewModel
@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.Tasks.route) {
        composable(Routes.Login.route) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController, loginViewModel)
        }
        composable(Routes.Tasks.route) {
            val tasksViewModel = hiltViewModel<TasksViewModel>()
            TaskScreen(navController, tasksViewModel)
        }
    }
}