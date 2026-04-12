package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue

actual class Intent(
    val extras: Map<String, Any> = emptyMap(),
) {
    actual fun getLongExtra(
        key: String,
        defaultValue: Long,
    ): Long = extras[key] as? Long ?: defaultValue

    actual fun getBooleanExtra(
        key: String,
        defaultValue: Boolean,
    ): Boolean = extras[key] as? Boolean ?: defaultValue
}

@Composable
actual fun RememberDeepLinkHandler(onDeepLink: (Intent) -> Unit) {
    val currentOnDeepLink by rememberUpdatedState(onDeepLink)

    DisposableEffect(Unit) {
        val observer =
            NSNotificationCenter.defaultCenter.addObserverForName(
                name = DeepLinkKeys.KEY_FCM_NOTIFICATION, // 커스텀 알림 이름 (AppDelegate에서 쏴줘야 함)
                `object` = null,
                queue = NSOperationQueue.mainQueue,
            ) { notification ->
                val notificationToExpand =
                    notification
                        ?.userInfo
                        ?.get(DeepLinkKeys.KEY_ANNOUNCEMENT_ID)
                        ?.toString()
                        ?.toLongOrNull() ?: return@addObserverForName
                currentOnDeepLink(
                    Intent(
                        mapOf(
                            DeepLinkKeys.KEY_NOTICE_ID_TO_EXPAND to notificationToExpand,
                            DeepLinkKeys.KEY_CAN_NAVIGATE_TO_NEWS to true,
                        ),
                    ),
                )
            }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }
}
