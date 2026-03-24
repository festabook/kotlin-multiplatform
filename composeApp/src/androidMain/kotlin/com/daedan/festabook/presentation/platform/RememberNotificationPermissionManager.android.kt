package com.daedan.festabook.presentation.platform

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.daedan.festabook.presentation.NotificationPermissionManager

@Composable
actual fun rememberNotificationPermissionManager(
    notificationPermissionManagerFactory: NotificationPermissionManager.Factory,
    onPermissionGrant: () -> Unit,
    onPermissionDeny: () -> Unit,
): NotificationPermissionManager {
    val permissionLauncher =
        rememberPermissionLauncher { isGranted ->
            if (isGranted) onPermissionGrant() else onPermissionDeny()
        }
    val context = LocalContext.current
    val activity = LocalActivity.current

    return remember(notificationPermissionManagerFactory, permissionLauncher) {
        notificationPermissionManagerFactory.create(
            context = context,
            launchPermission = { onResult -> permissionLauncher(onResult) },
            shouldShowRationale = { permission -> shouldShowRationale(permission, activity) },
        )
    }
}
