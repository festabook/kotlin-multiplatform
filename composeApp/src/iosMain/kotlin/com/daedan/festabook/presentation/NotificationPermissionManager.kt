package com.daedan.festabook.presentation

import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

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
        positiveText: String,
        negativeText: String,
    ) {
        launchPermission("")
    }

    actual fun showRationaleDialog(
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
    ) {
        shouldShowRationale("")
    }
}
