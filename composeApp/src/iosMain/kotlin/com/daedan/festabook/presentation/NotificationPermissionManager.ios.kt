package com.daedan.festabook.presentation

import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter

@AssistedInject
actual class NotificationPermissionManager actual constructor(
    @Assisted contextFactory: ContextFactory,
    @Assisted private val launchPermission: (String) -> Unit,
    @Assisted private val shouldShowRationale: (String) -> Boolean,
    @Assisted("granted") private val onPermissionGranted: () -> Unit,
    @Assisted("denied") private val onPermissionDenied: () -> Unit,
) {
    @AssistedFactory
    actual interface Factory {
        actual fun create(
            contextFactory: ContextFactory,
            launchPermission: (String) -> Unit,
            shouldShowRationale: (String) -> Boolean,
            @Assisted(value = "granted") onPermissionGranted: () -> Unit,
            @Assisted(value = "denied") onPermissionDenied: () -> Unit,
        ): NotificationPermissionManager
    }

    actual fun requestNotificationPermission(
        title: String,
        message: String,
        confirmText: String,
        cancelText: String,
    ) {
        val center = UNUserNotificationCenter.currentNotificationCenter()

        center.getNotificationSettingsWithCompletionHandler { settings ->
            when (settings?.authorizationStatus) {
                UNAuthorizationStatusAuthorized,
                UNAuthorizationStatusProvisional,
                -> {
                    // 이미 허용 됐을 때
                    onPermissionGranted()
                }

                UNAuthorizationStatusNotDetermined -> {
                    // 처음 요청 일 때
                    launchPermission("")
                }

                UNAuthorizationStatusDenied -> {
                    // 이미 거부 했을 때
                    showRationaleDialog(title, message, confirmText, cancelText)
                }

                else -> {
                    onPermissionDenied()
                }
            }
        }
    }

    actual fun showRationaleDialog(
        title: String,
        message: String,
        confirmText: String,
        cancelText: String,
    ) {
        val alert =
            UIAlertController.alertControllerWithTitle(
                title = title,
                message = message,
                preferredStyle = UIAlertControllerStyleAlert,
            )
        val confirmAction =
            UIAlertAction.actionWithTitle(confirmText, UIAlertActionStyleDefault) {
                onPermissionDenied()
            }
        alert.addAction(confirmAction)

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(alert, animated = true, completion = null)
    }
}
