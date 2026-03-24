package com.daedan.festabook.presentation.setting.component.platform

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
actual fun rememberPermissionLauncher(onResult: (Boolean) -> Unit): (String) -> Unit {
    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            onResult(isGranted)
        }

    return { permission ->
        launcher.launch(permission)
    }
}

actual fun shouldShowRationale(
    permission: String,
    activity: Any?,
): Boolean = (activity as? ComponentActivity)?.shouldShowRequestPermissionRationale(permission) ?: false
