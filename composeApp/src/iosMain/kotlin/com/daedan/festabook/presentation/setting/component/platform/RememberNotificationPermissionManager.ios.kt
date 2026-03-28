package com.daedan.festabook.presentation.setting.component.platform

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
            launchPermission = { permission -> permissionLauncher(permission) },
        )
    }
}
