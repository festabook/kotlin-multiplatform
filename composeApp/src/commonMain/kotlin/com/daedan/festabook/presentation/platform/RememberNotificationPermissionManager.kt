package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.daedan.festabook.presentation.ContextFactory
import com.daedan.festabook.presentation.NotificationPermissionManager

@Composable
fun rememberNotificationPermissionManager(
    contextFactory: ContextFactory,
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
            contextFactory = contextFactory,
            launchPermission = { onResult -> permissionLauncher(onResult) },
            shouldShowRationale = { permission -> shouldShowRationale(permission, contextFactory) },
            onPermissionGranted = onPermissionGrant,
            onPermissionDenied = onPermissionDeny,
        )
    }
}

@Composable
expect fun rememberPermissionLauncher(onResult: (Boolean) -> Unit): (String) -> Unit

expect fun shouldShowRationale(
    permission: String,
    contextFactory: ContextFactory,
): Boolean
