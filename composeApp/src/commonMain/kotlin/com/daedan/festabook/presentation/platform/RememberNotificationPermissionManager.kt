package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import com.daedan.festabook.presentation.NotificationPermissionManager

@Composable
expect fun rememberNotificationPermissionManager(
    notificationPermissionManagerFactory: NotificationPermissionManager.Factory,
    onPermissionGrant: () -> Unit,
    onPermissionDeny: () -> Unit,
): NotificationPermissionManager

@Composable
expect fun rememberPermissionLauncher(onResult: (Boolean) -> Unit): (String) -> Unit

expect fun shouldShowRationale(
    permission: String,
    activity: Any?,
): Boolean
