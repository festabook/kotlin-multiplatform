package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.daedan.festabook.di.FestabookAppGraph
import com.daedan.festabook.di.IosAppGraph
import dev.zacsweers.metro.createGraph

@Composable
actual fun rememberAppGraph(): FestabookAppGraph = remember { createGraph<IosAppGraph>() }
