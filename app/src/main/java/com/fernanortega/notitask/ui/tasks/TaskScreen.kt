package com.fernanortega.notitask.ui.tasks

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
        TaskBody(viewModel, navController)
    } else {
        LoadingComponent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBody(viewModel: TasksViewModel, navController: NavController) {
    viewModel.getTasks()
    val username: String by viewModel.username.observeAsState(initial = "")
    val tasks: List<TaskModel> by viewModel.tasks.observeAsState(initial = emptyList())
    val isUILoading: Boolean by viewModel.isUILoading.observeAsState(initial = false)
    val listState = rememberLazyListState()
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Routes.CreateTask.route) },
                expanded = expandedFab,
                icon = { Icon(Icons.Filled.Add, "Localized Description") },
                text = { Text(text = "Add task") },
            )
        }, topBar = { TopBar(username, tasks.size, scrollBehavior) }) {
        Column(Modifier.padding(it)) {
            BottomTasks(tasks, isUILoading, listState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(username: String, tasks: Int, scrollBehavior: TopAppBarScrollBehavior) {
    var setTime by rememberSaveable {
        mutableStateOf("")
    }
    when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..4 -> setTime = stringResource(id = R.string.good_evening)
        in 5..11 -> setTime = stringResource(id = R.string.good_morning)
        in 12..18 -> setTime = stringResource(id = R.string.good_afternoon)
        in 19..23 -> setTime = stringResource(id = R.string.good_evening)
    }

    val showSize = tasks.takeIf {
        it != 0
    } ?: "0"

    LargeTopAppBar(scrollBehavior = scrollBehavior, title = {
        Column() {
            if(scrollBehavior.state.collapsedFraction < 0.5) {
                Text(
                    text = setTime.plus(", $username"),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = FontWeight.Light
                )
            }
            Text(
                text = stringResource(id = R.string.your_tasks).plus(" ($showSize)"),
            )
        }
    })
}

@Composable
fun BottomTasks(tasks: List<TaskModel>, isUILoading: Boolean, state: LazyListState) {
    if (tasks.isEmpty()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val painter =
                painterResource(id = if (isSystemInDarkTheme()) R.drawable.empty_list_dark else R.drawable.empty_list_light)
            Image(
                painter = painter,
                contentDescription = "Empty image",
                modifier = Modifier
                    .weight(0.5f, fill = false)
                    .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = stringResource(id = R.string.empty_tasks))
        }
    } else {
        if (isUILoading) {
            LoadingComponent()
        } else {
            LazyColumn(
                state = state,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 88.dp)
            ) {
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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = task.taskTitle,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Medium
            )
            if (task.taskBody.isNotBlank()) {
                Text(
                    text = task.taskBody,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                )
            }
        }
    }
}
