package com.fernanortega.notitask.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun responsiveHandler() : Array<Int> {
    val window = LocalConfiguration.current

    val width = remember {
        mutableStateOf(window.screenWidthDp)
    }

    val height = remember {
        mutableStateOf(window.screenHeightDp)
    }

    val orientation = remember {
        mutableStateOf(window.orientation)
    }

    return arrayOf(width.value, height.value, orientation.value)
}