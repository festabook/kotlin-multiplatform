package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    return remember(notificationPermissionManagerFactory, permissionLauncher) {
        notificationPermissionManagerFactory.create(
            onPermissionGranted = onPermissionGrant,
            onPermissionDenied = onPermissionDeny,
            launchPermission = { permission -> permissionLauncher(permission) },
        )
    }
}
