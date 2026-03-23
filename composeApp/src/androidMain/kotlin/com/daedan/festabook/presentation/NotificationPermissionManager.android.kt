package com.daedan.festabook.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
actual class NotificationPermissionManager(
    @Assisted private val context: Context,
    @Assisted private val launchPermission: (String) -> Unit,
    @Assisted private val shouldShowRationale: (String) -> Boolean,
    @Assisted("granted") private val onPermissionGranted: () -> Unit,
    @Assisted("denied") private val onPermissionDenied: () -> Unit,
) {
    @AssistedFactory
    actual interface Factory {
        fun create(
            context: Context,
            launchPermission: (String) -> Unit,
            shouldShowRationale: (String) -> Boolean,
            @Assisted("granted") onPermissionGranted: () -> Unit,
            @Assisted("denied") onPermissionDenied: () -> Unit,
        ): NotificationPermissionManager
    }

    actual suspend fun checkPermission(): PermissionState =
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED -> {
                PermissionState.GRANTED
            }

            shouldShowRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                PermissionState.NEED_RATIONALE
            }

            else -> {
                PermissionState.NEED_REQUEST
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    actual fun requestPermission() {
        launchPermission(Manifest.permission.POST_NOTIFICATIONS)
    }
}
