package com.fernanortega.notitask.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fernanortega.notitask.R
import com.fernanortega.notitask.viewmodel.TasksViewModel

@Composable
fun DeleteTaskDialog(
    id: Long,
    title: String,
    show: Boolean,
    body: String,
    viewModel: TasksViewModel
) {
    if (show) {
        AlertDialog(onDismissRequest = {
            viewModel.closeDeleteDialog()
            viewModel.closeDialog()
        }, icon = {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon")
        }, title = {
            Text(text = stringResource(id = R.string.delete_task_title))
        }, text = {
            Column(Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.delete_task_body))
                Divider(
                    thickness = 1.dp, modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                )
                Text(text = title, fontWeight = FontWeight.SemiBold)
                if (body.isNotBlank()) {
                    Text(text = body, overflow = TextOverflow.Ellipsis, maxLines = 3)
                }
                Divider(
                    thickness = 1.dp, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                )
            }
        }, dismissButton = {
            TextButton(onClick = { viewModel.closeDialog() }) {
                Text(text = stringResource(id = R.string.cancel_text))
            }
        }, confirmButton = {
            TextButton(onClick = {
                viewModel.deleteTask(id)
                viewModel.closeDeleteDialog()
                viewModel.closeDialog()
            }) {
                Text(text = stringResource(id = R.string.delete_task_button))
            }
        })
    }
}