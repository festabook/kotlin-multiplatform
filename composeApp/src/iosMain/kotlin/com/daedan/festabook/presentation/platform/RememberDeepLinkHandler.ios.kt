package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.collectLatest

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
actual fun RememberDeepLinkHandler(onNotificationClicked: (announcementId: Long, festivalIdChanged: Boolean) -> Unit) {
    val currentOnNotificationClicked by rememberUpdatedState(onNotificationClicked)

    LaunchedEffect(Unit) {
        PendingFcmNotification.pending.collectLatest { data ->
            currentOnNotificationClicked(data.announcementId, data.festivalIdChanged)
        }
    }
}
