package com.daedan.festabook.presentation.platform

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.daedan.festabook.presentation.ContextFactory

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
    contextFactory: ContextFactory,
): Boolean {
    val activity = contextFactory.getActivity() as? ComponentActivity
    return activity?.shouldShowRequestPermissionRationale(permission) ?: false
}
