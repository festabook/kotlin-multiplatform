package com.daedan.festabook.presentation

expect class NotificationPermissionManager(
    contextFactory: ContextFactory,
    launchPermission: (String) -> Unit,
    shouldShowRationale: (String) -> Boolean,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {
    interface Factory {
        fun create(
            contextFactory: ContextFactory,
            launchPermission: (String) -> Unit,
            shouldShowRationale: (String) -> Boolean,
            onPermissionGranted: () -> Unit = {},
            onPermissionDenied: () -> Unit = {},
        ): NotificationPermissionManager
    }

    fun requestNotificationPermission(
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
    )

    fun showRationaleDialog(
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
    )
}
