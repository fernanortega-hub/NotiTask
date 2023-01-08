package com.fernanortega.notitask.ui.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.fernanortega.notitask.viewmodel.TasksViewModel

@Composable
fun TaskScreen(navController: NavController, viewModel: TasksViewModel) {
    val username: String by viewModel.username.observeAsState(initial = "")
    Column(Modifier.fillMaxSize()) {
        Text(text = "Hola $username")
    }
}