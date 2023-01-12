package com.fernanortega.notitask.ui.tasks.create_task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fernanortega.notitask.R
import com.fernanortega.notitask.ui.navigation.Routes
import com.fernanortega.notitask.viewmodel.CreateTaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(viewModel: CreateTaskViewModel, navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = { TopAppBar(scrollBehavior, navController) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Form(
                viewModel,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                ),
                navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(scrollBehavior: TopAppBarScrollBehavior, navController: NavController) {
    LargeTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.title_create),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Routes.Tasks.route) }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(viewModel: CreateTaskViewModel, modifier: Modifier, navController: NavController) {
    val title: String by viewModel.title.observeAsState(initial = "")
    val body: String by viewModel.body.observeAsState(initial = "")
    val isButtonEnabled: Boolean by viewModel.isButtonEnabled.observeAsState(initial = false)
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .widthIn(min = 280.dp, max = 480.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { viewModel.onTextChange(it, body) },
            maxLines = 1,
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.task_title)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            })
        )
        Spacer(modifier = Modifier.size(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = body,
            onValueChange = { viewModel.onTextChange(title, it) },
            maxLines = 4,
            label = { Text(text = stringResource(id = R.string.task_description)) },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Routes.Tasks.route) }) {
                Text(text = stringResource(id = R.string.cancel_text))
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.createTask()
                    navController.navigate(Routes.Tasks.route)
                },
                enabled = isButtonEnabled
            ) {
                Text(text = stringResource(id = R.string.title_create))
            }
        }
        Spacer(modifier = Modifier.size(32.dp))
    }
}