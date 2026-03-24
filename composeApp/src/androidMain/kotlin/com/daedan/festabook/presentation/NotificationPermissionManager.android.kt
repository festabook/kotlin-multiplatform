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
) {
    @AssistedFactory
    actual interface Factory {
        fun create(
            context: Context,
            launchPermission: (String) -> Unit,
            shouldShowRationale: (String) -> Boolean,
        ): NotificationPermissionManager
    }

    actual suspend fun checkPermission(): PermissionState {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return PermissionState.GRANTED
        }
        val permission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
        val rationale = shouldShowRationale(Manifest.permission.POST_NOTIFICATIONS)

        return when {
            permission == PackageManager.PERMISSION_GRANTED -> PermissionState.GRANTED
            rationale -> PermissionState.NEED_RATIONALE
            else -> PermissionState.NEED_REQUEST
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    actual fun requestPermission() {
        launchPermission(Manifest.permission.POST_NOTIFICATIONS)
    }
}
