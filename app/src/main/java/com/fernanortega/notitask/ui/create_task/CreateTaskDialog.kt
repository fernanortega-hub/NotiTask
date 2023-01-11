package com.fernanortega.notitask.ui.create_task

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fernanortega.notitask.R
import com.fernanortega.notitask.viewmodel.TasksViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CreateTaskDialog(viewModel: TasksViewModel, onDismiss: () -> Unit, show: Boolean) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    if (show) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Scaffold(topBar = { TopAppBar(scrollBehavior, onDismiss) }) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = CenterHorizontally) {
                    Form(
                        viewModel,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = it.calculateTopPadding(),
                            bottom = it.calculateBottomPadding()
                        ),
                        onDismiss
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(scrollBehavior: TopAppBarScrollBehavior, onDismiss: () -> Unit) {
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
            IconButton(onClick = { onDismiss() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(viewModel: TasksViewModel, modifier: Modifier, onDismiss: () -> Unit) {
    val title: String by viewModel.title.observeAsState("")
    val body: String by viewModel.body.observeAsState("")
    val isButtonEnabled: Boolean by viewModel.isButtonEnabled.observeAsState(false)
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .widthIn(min = 280.dp, max = 480.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally),
            value = title,
            onValueChange = { viewModel.onTextChange(it, body) },
            maxLines = 1,
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.title_create)) },
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
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally),
            value = body,
            onValueChange = { viewModel.onTextChange(title, it) },
            maxLines = 4,
            label = { Text(text = stringResource(id = R.string.task_description)) },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            Modifier
                .fillMaxWidth()
                .align(CenterHorizontally), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = { onDismiss() }) {
                Text(text = stringResource(id = R.string.cancel_text))
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.createTask()
                    onDismiss()
                },
                enabled = isButtonEnabled
            ) {
                Text(text = stringResource(id = R.string.title_create))
            }
        }
        Spacer(modifier = Modifier.size(32.dp))
    }
}