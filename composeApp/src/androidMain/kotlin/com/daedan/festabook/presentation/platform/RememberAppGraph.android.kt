package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.daedan.festabook.di.FestabookAppGraph
import com.daedan.festabook.di.appGraph

@Composable
actual fun rememberAppGraph(): FestabookAppGraph {
    val context = LocalContext.current
    return remember(context) { context.appGraph }
}
