package com.fernanortega.notitask.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fernanortega.notitask.ui.login.LoginScreen
import com.fernanortega.notitask.ui.tasks.TaskScreen
import com.fernanortega.notitask.ui.tasks.create_task.CreateTaskScreen
import com.fernanortega.notitask.viewmodel.CreateTaskViewModel
import com.fernanortega.notitask.viewmodel.LoginViewModel
import com.fernanortega.notitask.viewmodel.TasksViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(navController: NavHostController = rememberAnimatedNavController()) {
    AnimatedNavHost(navController = navController, startDestination = Routes.Tasks.route) {
        composable(Routes.Login.route) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController, loginViewModel)
        }
        composable(Routes.Tasks.route, enterTransition = {
            slideIn(
                animationSpec = tween(300, easing = CubicBezierEasing(0.2f, 0f, 0f, 1f))
            ) {
                IntOffset(it.width / 4, 100)
            }
        }, exitTransition = {
            fadeOut(
                animationSpec = tween(250, easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f))
            )
        }) {
            val tasksViewModel = hiltViewModel<TasksViewModel>()
            TaskScreen(navController, tasksViewModel)
        }
        composable(Routes.CreateTask.route, enterTransition = {
            scaleIn(
                animationSpec = tween(300, easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)),
                transformOrigin = TransformOrigin(0.95f, 0.95f)
            )
        }, exitTransition = {
            scaleOut(
                animationSpec = tween(250, easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)),
                transformOrigin = TransformOrigin(0.95f, 0.95f)
            )
        }) {
            val createTaskViewModel = hiltViewModel<CreateTaskViewModel>()
            CreateTaskScreen(createTaskViewModel, navController)
        }
    }
}