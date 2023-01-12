package com.fernanortega.notitask.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fernanortega.notitask.R

@Composable
fun EmptyListTasks() {
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
}