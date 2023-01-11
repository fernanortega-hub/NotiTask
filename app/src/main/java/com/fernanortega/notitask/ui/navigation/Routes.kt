package com.fernanortega.notitask.ui.navigation

sealed class Routes(val route: String) {
    object Login: Routes("login")
    object Tasks: Routes("tasks")
    object CreateTask: Routes("create-task")
    object EditTask: Routes("task/{id}") {
        fun createRoute(id: Long) = "task/$id"
    }
}
