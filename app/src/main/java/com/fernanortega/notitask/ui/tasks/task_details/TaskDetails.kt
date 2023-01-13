package com.fernanortega.notitask.ui.tasks.task_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fernanortega.notitask.R
import com.fernanortega.notitask.ui.components.dialogs.DeleteTaskDialog
import com.fernanortega.notitask.viewmodel.TasksViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TaskDetails(
    onDismiss: () -> Unit,
    viewModel: TasksViewModel,
    showDialog: Boolean,
    taskId: Long,
    taskTitle: String,
    taskBody: String
) {
    val showDeleteDialog by viewModel.showDeleteDialog.observeAsState(false)
    DeleteTaskDialog(taskId, taskTitle, showDeleteDialog, taskBody, viewModel)

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                onDismiss()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true
            )
        ) {
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            Scaffold(topBar = {
                AppBarTaskDetails(
                    title = taskTitle,
                    scrollBehavior = scrollBehavior,
                    onDismiss = { onDismiss() }, showDialogForDelete = {
                        viewModel.showDeleteDialog()
                    })
            }) {
                Form(viewModel, modifier = Modifier.fillMaxSize()) {
                    onDismiss()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarTaskDetails(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onDismiss: () -> Unit,
    showDialogForDelete: () -> Unit
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { onDismiss() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = { showDialogForDelete() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon")
            }
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit icon")
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(viewModel: TasksViewModel, modifier: Modifier, onDismiss: () -> Unit) {
    val title: String by viewModel.taskTitle.observeAsState("")
    val body: String by viewModel.taskBody.observeAsState("")
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
//                    viewModel.createTask()
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