package com.fernanortega.notitask.ui.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun BackHandler() {
    val activity = LocalContext.current as Activity
    androidx.activity.compose.BackHandler {
        activity.finish()
    }
}