package com.daedan.festabook.presentation.platform

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.util.Consumer

actual typealias Intent = Intent

@Composable
actual fun RememberDeepLinkHandler(onNotificationClicked: (announcementId: Long, festivalIdChanged: Boolean) -> Unit) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity ?: return
    val currentOnNotificationClicked by rememberUpdatedState(onNotificationClicked)

    LaunchedEffect(Unit) {
        activity.intent.handleDeepLink(currentOnNotificationClicked)
    }

    DisposableEffect(activity) {
        val listener =
            Consumer<Intent> { intent ->
                intent.handleDeepLink(currentOnNotificationClicked)
            }
        activity.addOnNewIntentListener(listener)
        onDispose { activity.removeOnNewIntentListener(listener) }
    }
}

private fun Intent.handleDeepLink(onNotificationClicked: (announcementId: Long, festivalIdChanged: Boolean) -> Unit) {
    val announcementId =
        getLongExtra(DeepLinkKeys.KEY_NOTICE_ID_TO_EXPAND, DeepLinkKeys.INITIALIZED_ID)
    if (announcementId != DeepLinkKeys.INITIALIZED_ID) {
        onNotificationClicked(announcementId, false)
        removeExtra(DeepLinkKeys.KEY_NOTICE_ID_TO_EXPAND)
        removeExtra(DeepLinkKeys.KEY_CAN_NAVIGATE_TO_NEWS)
    }
}
