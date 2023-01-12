package com.fernanortega.notitask.ui.tasks

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fernanortega.notitask.R
import com.fernanortega.notitask.model.domain.TaskModel
import com.fernanortega.notitask.ui.components.EmptyListTasks
import com.fernanortega.notitask.ui.components.LoadingComponent
import com.fernanortega.notitask.ui.components.dialogs.DeleteTaskDialog
import com.fernanortega.notitask.ui.navigation.Routes
import com.fernanortega.notitask.ui.utils.BackHandler
import com.fernanortega.notitask.ui.utils.responsiveHandler
import com.fernanortega.notitask.viewmodel.TasksViewModel
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
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

    val metrics = responsiveHandler()
    if (userExists) {
        TaskBody(viewModel, navController, metrics)
    } else {
        LoadingComponent()
    }
}

@ExperimentalAnimationApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBody(viewModel: TasksViewModel, navController: NavController, metrics: Array<Int>) {
    viewModel.getTasks()
    val username: String by viewModel.username.observeAsState("")
    val taskId: Long by viewModel.taskId.observeAsState(Long.MIN_VALUE)
    val taskTitle: String by viewModel.taskTitle.observeAsState("")
    val taskBody: String by viewModel.taskBody.observeAsState("")
    val tasks: List<TaskModel> by viewModel.tasks.observeAsState(emptyList())
    val listState = rememberLazyGridState()
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val showActionButtons: Boolean by viewModel.showButtons.observeAsState(false)
    val showDialog: Boolean by viewModel.showDialog.observeAsState(false)


    DeleteTaskDialog(taskId, taskTitle, showDialog, taskBody, viewModel)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            if (!showActionButtons) {
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate(Routes.CreateTask.route)
                    },
                    expanded = expandedFab,
                    icon = { Icon(Icons.Filled.Add, "add task icon") },
                    text = { Text(text = stringResource(R.string.add_task)) },
                )
            }
        }, topBar = {
            TopBar(username, tasks.size, scrollBehavior, showActionButtons, showDialogForDelete = {
                viewModel.showDialog()
            }, navigateToEdit = {
                navController.navigate(Routes.EditTask.createRoute(taskId))
            }, { viewModel.hideButtons() })
        }) {
        Column(Modifier.padding(it)) {
            BottomTasks(
                tasks,
                listState,
                viewModel,
                showActionButtons,
                metrics
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    username: String,
    tasks: Int,
    scrollBehavior: TopAppBarScrollBehavior,
    showActionButtons: Boolean,
    showDialogForDelete: () -> Unit,
    navigateToEdit: () -> Unit,
    hideButtons: () -> Unit
) {
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
        Column {
            if (scrollBehavior.state.collapsedFraction < 0.5) {
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
    }, actions = {
        if (showActionButtons) {
            IconButton(onClick = { showDialogForDelete() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon")
            }
            IconButton(onClick = { showDialogForDelete() }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit icon")
            }
        }
    }, navigationIcon = {
        if (showActionButtons) {
            IconButton(onClick = { hideButtons() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
            }
        }
    })
}

@Composable
fun BottomTasks(
    tasks: List<TaskModel>,
    state: LazyGridState,
    viewModel: TasksViewModel,
    showActionButtons: Boolean,
    metrics: Array<Int>
) {
    val hapticFeedback = LocalHapticFeedback.current
    val changeGridCells = remember { mutableStateOf(1) }

    when {
        metrics[2] == 1 -> {
            if (metrics[0].dp > 600.dp) {
                changeGridCells.value = 1
            } else if (metrics[0].dp > 720.dp) {
                changeGridCells.value = 3
            } else if (metrics[0].dp > 1024.dp) {
                changeGridCells.value = 4
            }
        }
        metrics[2] == 2 -> {
            if (metrics[0].dp > 600.dp) {
                changeGridCells.value = 2
            } else if (metrics[0].dp > 720.dp) {
                changeGridCells.value = 4
            }
        }
    }
    if (tasks.isEmpty()) {
        EmptyListTasks()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(changeGridCells.value),
            state = state,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 88.dp,
                top = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks, key = {
                it.id
            }) { task ->
                TaskItem(
                    task,
                    function = {
                        viewModel.showButtons()
                        viewModel.onInfoChange(task.id, task.taskTitle, task.taskBody)
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: TaskModel,
    modifier: Modifier = Modifier,
    function: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .clickable {
                    function()
                }
        ) {
            Column(Modifier.padding(8.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = task.taskTitle,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontWeight = FontWeight.Medium
                )
                if (task.taskBody.isNotBlank()) {
                    Text(
                        text = task.taskBody,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
