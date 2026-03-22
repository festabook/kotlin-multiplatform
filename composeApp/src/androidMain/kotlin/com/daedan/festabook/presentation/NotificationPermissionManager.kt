package com.daedan.festabook.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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
            @Assisted("granted") onPermissionGranted: () -> Unit,
            @Assisted("denied") onPermissionDenied: () -> Unit,
        ): NotificationPermissionManager
    }

    private val context = contextFactory.activityContext

    actual fun requestNotificationPermission(
        title: String,
        message: String,
        confirmText: String,
        cancelText: String,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED -> {
                    // 이미 권한이 허용됨
//                    Timber.d("Notification permission already granted")
                    onPermissionGranted()
                }

                shouldShowRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // 이전에 거부했지만 "다시 묻지 않음"을 선택하지 않은 경우
                    // 권한이 필요한 이유를 설명하는 UI(예: AlertDialog)를 표시
//                    Timber.d("Show rationale for notification permission")
                    showRationaleDialog(
                        title = title,
                        message = message,
                        confirmText = confirmText,
                        cancelText = cancelText,
                    )
                }

                else -> {
                    // 권한이 없으며, 이전에 "다시 묻지 않음"을 선택했거나 첫 요청인 경우
                    // 바로 권한 요청 다이얼로그 표시
//                    Timber.d("Requesting notification permission for the first time or after 'don't ask again'")
                    launchPermission(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
//            Timber.d("Notification permission not required for API < 33")
        }
    }

    actual fun showRationaleDialog(
        title: String,
        message: String,
        confirmText: String,
        cancelText: String,
    ) {
        AlertDialog
            .Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(confirmText) { dialog, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launchPermission(Manifest.permission.POST_NOTIFICATIONS)
                }
                dialog.dismiss()
            }.setNegativeButton(cancelText) { dialog, _ ->
//                Timber.d("Notification permission denied")
                onPermissionDenied()
                dialog.dismiss()
            }.setCancelable(false)
            .show()
    }
}
