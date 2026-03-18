package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.daedan.festabook.di.FestabookAppGraph
import com.daedan.festabook.di.IosAppGraph
import com.daedan.festabook.presentation.splash.platform.AppVersionManager

@Composable
actual fun rememberAppVersionManager(
    appGraph: FestabookAppGraph,
    onUpdateSuccess: () -> Unit,
    onUpdateFailure: () -> Unit,
): AppVersionManager =
    remember {
        (appGraph as IosAppGraph).appVersionManager
    }
