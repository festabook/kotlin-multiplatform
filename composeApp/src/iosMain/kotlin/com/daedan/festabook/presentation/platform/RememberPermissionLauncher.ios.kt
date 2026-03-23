package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter

@Composable
actual fun rememberPermissionLauncher(onResult: (Boolean) -> Unit): (String) -> Unit =
    { _ ->
        val center = UNUserNotificationCenter.currentNotificationCenter()

        center.requestAuthorizationWithOptions(
            options =
                UNAuthorizationOptionAlert or
                    UNAuthorizationOptionSound or
                    UNAuthorizationOptionBadge,
        ) { granted, _ -> onResult(granted) }
    }

actual fun shouldShowRationale(
    permission: String,
    activity: Any?,
): Boolean = false
