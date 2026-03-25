package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.daedan.festabook.DefaultFestabookAppDelegate
import com.daedan.festabook.di.FestabookAppGraph

@Composable
actual fun rememberAppGraph(): FestabookAppGraph = remember { DefaultFestabookAppDelegate.appGraph }
