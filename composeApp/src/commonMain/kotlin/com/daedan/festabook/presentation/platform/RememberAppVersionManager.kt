package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import com.daedan.festabook.di.FestabookAppGraph
import com.daedan.festabook.presentation.splash.platform.AppVersionManager

@Composable
expect fun rememberAppVersionManager(
    appGraph: FestabookAppGraph,
    onUpdateSuccess: () -> Unit,
    onUpdateFailure: () -> Unit,
): AppVersionManager
