package com.fernanortega.notitask.ui.navigation

import com.fernanortega.notitask.viewmodel.LoginViewModel
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fernanortega.notitask.ui.login.LoginScreen
import com.fernanortega.notitask.ui.tasks.TaskScreen
import com.fernanortega.notitask.viewmodel.TasksViewModel

@Composable
fun NavGraph(navController: NavHostController = rememberNavController(), firstDestination: String = Routes.Login.route) {
    NavHost(navController = navController, startDestination = firstDestination) {
        composable(Routes.Login.route) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController, loginViewModel)
        }
        composable(Routes.Tasks.route) {
            val tasksViewModel = hiltViewModel<TasksViewModel>()
            tasksViewModel.getUserInfo()
            TaskScreen(navController, tasksViewModel)
        }
    }
}