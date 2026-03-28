package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue

actual class Intent(
    val notificationToExpand: Long,
    val canNavigateToNews: Boolean,
) {
    actual fun getLongExtra(
        key: String,
        defaultValue: Long,
    ): Long = notificationToExpand

    actual fun getBooleanExtra(
        key: String,
        defaultValue: Boolean,
    ): Boolean = canNavigateToNews
}

@Composable
actual fun RememberDeepLinkHandler(onDeepLink: (Intent) -> Unit) {
    val currentOnDeepLink by rememberUpdatedState(onDeepLink)

    DisposableEffect(Unit) {
        val observer =
            NSNotificationCenter.defaultCenter.addObserverForName(
                name = "fcmNewsNotification", // 커스텀 알림 이름 (AppDelegate에서 쏴줘야 함)
                `object` = null,
                queue = NSOperationQueue.mainQueue,
            ) { notification ->
                val notificationToExpand =
                    notification
                        ?.userInfo
                        ?.get("announcementId")
                        ?.toString()
                        ?.toLongOrNull() ?: return@addObserverForName
                currentOnDeepLink(Intent(notificationToExpand, true))
            }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }
}
