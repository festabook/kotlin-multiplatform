package com.daedan.festabook.presentation

import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume

@AssistedInject
actual class NotificationPermissionManager(
    @Assisted private val launchPermission: (String) -> Unit,
) {
    @AssistedFactory
    actual interface Factory {
        fun create(launchPermission: (String) -> Unit): NotificationPermissionManager
    }

    private val center = UNUserNotificationCenter.currentNotificationCenter()

    actual suspend fun checkPermission(): PermissionState =
        suspendCancellableCoroutine { const ->
            center.getNotificationSettingsWithCompletionHandler { settings ->
                val result =
                    when (settings?.authorizationStatus) {
                        UNAuthorizationStatusAuthorized,
                        UNAuthorizationStatusProvisional,
                        -> {
                            PermissionState.GRANTED
                        }

                        else -> {
                            PermissionState.DENIED
                        }
                    }
                const.resume(result)
            }
        }

    actual fun requestPermission() {
        launchPermission("")
    }
}
