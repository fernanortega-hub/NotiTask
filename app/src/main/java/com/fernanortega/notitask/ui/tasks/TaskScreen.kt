package com.fernanortega.notitask.ui.tasks

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fernanortega.notitask.R
import com.fernanortega.notitask.ui.navigation.Routes
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
    val username: String by viewModel.username.observeAsState(initial = "")

    if (userExists) {
        TopBar(username)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun BackHandler() {
    val activity = LocalContext.current as Activity

    BackHandler {
        activity.finish()
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
        in 19..22 -> setTime = stringResource(id = R.string.good_evening)
    }


    Column(
        Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = setTime.plus(", $username"),
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 52.sp
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "12",
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 12.dp)
                        .border(
                            width = 0.dp,
                            color = MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(100.dp)
                        )
                        .graphicsLayer {
                            shape = RoundedCornerShape(100.dp)
                            clip = true
                        }
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(vertical = 4.dp, horizontal = 12.dp)
                )
                Text(
                    text = "Tasks",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

//@Composable
//@Preview(name = "task_screen", widthDp = 411, heightDp = 868, showSystemUi = true)
//fun Preview() {
//    TopBar(username = "Fernando")
//}