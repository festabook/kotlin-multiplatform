package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import com.daedan.festabook.di.FestabookAppGraph

@Composable
expect fun rememberAppGraph(): FestabookAppGraph
