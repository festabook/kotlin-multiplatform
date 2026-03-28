package com.daedan.festabook.presentation.setting.component.platform

import androidx.compose.runtime.Composable
import com.daedan.festabook.presentation.NotificationPermissionManager

@Composable
expect fun rememberNotificationPermissionManager(
    notificationPermissionManagerFactory: NotificationPermissionManager.Factory,
    onPermissionGrant: () -> Unit,
    onPermissionDeny: () -> Unit,
): NotificationPermissionManager
