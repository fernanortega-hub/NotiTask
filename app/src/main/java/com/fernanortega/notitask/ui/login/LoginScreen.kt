package com.fernanortega.notitask.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fernanortega.notitask.R
import com.fernanortega.notitask.ui.navigation.Routes
import com.fernanortega.notitask.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel) {
    val name: String by viewModel.name.observeAsState(initial = "")
    val isButtonEnabled: Boolean by viewModel.isButtonEnabled.observeAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.welcome_login),
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontStyle = MaterialTheme.typography.headlineSmall.fontStyle
        )
        Spacer(modifier = Modifier.size(32.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text(text = stringResource(id = R.string.input_name)) },
            placeholder = { Text(text = "John Doe") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            onClick = {
                viewModel.sendName()
                navController.navigate(Routes.Tasks.route)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isButtonEnabled
        ) {
            Text(text = stringResource(id = R.string.continue_button))
        }
    }
}
