package com.fernanortega.notitask.ui.tasks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fernanortega.notitask.R
import com.fernanortega.notitask.model.domain.TaskModel
import com.fernanortega.notitask.ui.components.LoadingComponent
import com.fernanortega.notitask.ui.navigation.Routes
import com.fernanortega.notitask.ui.utils.BackHandler
import com.fernanortega.notitask.viewmodel.TasksViewModel
import java.util.*

@Composable
fun TaskScreen(navController: NavController, viewModel: TasksViewModel) {
    viewModel.getUserInfo()

    BackHandler()

    var userExists by remember {
        mutableStateOf(false)
    }

    viewModel.userExists.observe(LocalLifecycleOwner.current) {
        if (!it) {
            userExists = false
            navController.navigate(Routes.Login.route)
        } else {
            userExists = true
        }
    }



    if (userExists) {
        TaskBody(viewModel)
    } else {
        LoadingComponent()
    }
}

@Composable
fun TaskBody(viewModel: TasksViewModel) {
    val username: String by viewModel.username.observeAsState(initial = "")
    val tasks: List<TaskModel> by viewModel.tasks.observeAsState(initial = emptyList())
    val isUILoading: Boolean by viewModel.isUILoading.observeAsState(initial = false)

    Column() {
        TopBar(username)
        BottomTasks(tasks, isUILoading)
    }
}

@Composable
fun TopBar(username: String) {
    var setTime by rememberSaveable {
        mutableStateOf("")
    }
    when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..4 -> setTime = stringResource(id = R.string.good_evening)
        in 5..11 -> setTime = stringResource(id = R.string.good_morning)
        in 12..18 -> setTime = stringResource(id = R.string.good_afternoon)
        in 19..23 -> setTime = stringResource(id = R.string.good_evening)
    }


    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = setTime.plus(", $username"),
            fontStyle = MaterialTheme.typography.titleSmall.fontStyle,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = stringResource(id = R.string.your_tasks).plus(" (1)"),
            fontStyle = MaterialTheme.typography.displaySmall.fontStyle,
            fontWeight = FontWeight.W300,
            fontSize = MaterialTheme.typography.displaySmall.fontSize
        )
    }
}

@Composable
fun BottomTasks(tasks: List<TaskModel>, isUILoading: Boolean) {

    if (tasks.isEmpty()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.empty_list_light),
                contentDescription = "Empty image",
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "No tienes tareas pendientes")
        }
    } else {
        if (isUILoading) {
            LoadingComponent()
        } else {
            LazyColumn {
                items(tasks, key = {
                    it.id
                }) { task ->
                    TaskItem(task)
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: TaskModel) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
    ) {
        Column(Modifier.fillMaxSize()) {
            Text(
                text = task.taskTitle,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = task.taskBody,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
            )
        }
    }
}
