package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL

actual class Intent(
    val url: NSURL,
)

@Composable
actual fun RememberDeepLinkHandler(onDeepLink: (Intent) -> Unit) {
    val currentOnDeepLink by rememberUpdatedState(onDeepLink)

    DisposableEffect(Unit) {
        val observer =
            NSNotificationCenter.defaultCenter.addObserverForName(
                name = "OpenURLNotification", // 커스텀 알림 이름 (AppDelegate에서 쏴줘야 함)
                `object` = null,
                queue = NSOperationQueue.mainQueue,
            ) { notification ->
                val url = notification?.userInfo?.get("url") as? NSURL ?: return@addObserverForName
                currentOnDeepLink(Intent(url))
            }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }
}
