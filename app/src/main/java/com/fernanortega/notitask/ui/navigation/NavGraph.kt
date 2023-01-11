package com.fernanortega.notitask.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fernanortega.notitask.ui.login.LoginScreen
import com.fernanortega.notitask.ui.tasks.TaskScreen
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
        composable(Routes.Tasks.route) {
            val tasksViewModel = hiltViewModel<TasksViewModel>()
            TaskScreen(navController, tasksViewModel)
        }
    }
}

//enterTransition = {
//    scaleIn(
//        animationSpec = tween(300, easing = EaseInCubic),
//        transformOrigin = TransformOrigin(0.95f, 0.95f)
//    )
//}, exitTransition = {
//    scaleOut(transformOrigin = TransformOrigin(0.95f, 0.95f)) + shrinkVertically(
//        shrinkTowards = Alignment.CenterVertically
//    )
//}, popEnterTransition = {
//    scaleIn(
//        animationSpec = tween(2500, easing = EaseInCubic),
//        transformOrigin = TransformOrigin(0.95f, 0.95f)
//    )
//}, popExitTransition = {
//    scaleOut(transformOrigin = TransformOrigin(0.95f, 0.95f))
//}