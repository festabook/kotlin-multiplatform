package com.daedan.festabook.presentation.platform

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.daedan.festabook.di.AndroidAppGraph
import com.daedan.festabook.di.FestabookAppGraph
import com.daedan.festabook.presentation.splash.platform.AppVersionManager

@Composable
actual fun rememberAppVersionManager(
    appGraph: FestabookAppGraph,
    onUpdateSuccess: () -> Unit,
    onUpdateFailure: () -> Unit,
): AppVersionManager {
    val factory = (appGraph as AndroidAppGraph).appVersionManagerFactory
    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult(),
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                onUpdateSuccess()
            } else {
                onUpdateFailure()
            }
        }

    return remember(factory, launcher) {
        factory.create(launcher)
    }
}
