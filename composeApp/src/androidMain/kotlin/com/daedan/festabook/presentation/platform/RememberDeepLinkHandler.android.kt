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

@Composable
actual fun RememberDeepLinkHandler(onNotificationClick: (action: FcmDeepLinkAction, festivalIdChanged: Boolean) -> Unit) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity ?: return
    val currentOnNotificationClicked by rememberUpdatedState(onNotificationClick)

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

private fun Intent.handleDeepLink(onNotificationClicked: (action: FcmDeepLinkAction, festivalIdChanged: Boolean) -> Unit) {
    val action = consumeDeepLinkAction() ?: return
    onNotificationClicked(action, false)
}

private fun Intent.consumeDeepLinkAction(): FcmDeepLinkAction? {
    val type = FcmMessageType.from(getStringExtra(DeepLinkKeys.KEY_TYPE))
    return when (type) {
        FcmMessageType.WAITING_CALL,
        FcmMessageType.WAITING_ALMOST_CALL,
        -> {
            removeExtra(DeepLinkKeys.KEY_TYPE)
            FcmDeepLinkAction.OpenMyWaiting
        }

        FcmMessageType.WAITING_PLACE_ACCESS_CANCEL -> {
            val placeId = getLongExtra(DeepLinkKeys.KEY_PLACE_ID, DeepLinkKeys.INITIALIZED_ID)
            removeExtra(DeepLinkKeys.KEY_TYPE)
            removeExtra(DeepLinkKeys.KEY_PLACE_ID)
            if (placeId == DeepLinkKeys.INITIALIZED_ID) null else FcmDeepLinkAction.OpenPlaceDetail(placeId)
        }

        FcmMessageType.ANNOUNCEMENT, null -> {
            val announcementId =
                getLongExtra(DeepLinkKeys.KEY_NOTICE_ID_TO_EXPAND, DeepLinkKeys.INITIALIZED_ID)
            val notificationSentAt =
                getLongExtra(DeepLinkKeys.KEY_NOTIFICATION_SENT_AT, DeepLinkKeys.INITIALIZED_ID)
            if (announcementId == DeepLinkKeys.INITIALIZED_ID) {
                null
            } else {
                removeExtra(DeepLinkKeys.KEY_TYPE)
                removeExtra(DeepLinkKeys.KEY_NOTICE_ID_TO_EXPAND)
                removeExtra(DeepLinkKeys.KEY_CAN_NAVIGATE_TO_NEWS)
                removeExtra(DeepLinkKeys.KEY_NOTIFICATION_SENT_AT)
                FcmDeepLinkAction.OpenAnnouncement(announcementId, notificationSentAt)
            }
        }
    }
}
