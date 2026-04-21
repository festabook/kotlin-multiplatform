package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.collectLatest

@Composable
actual fun RememberDeepLinkHandler(onNotificationClick: (announcementId: Long, festivalIdChanged: Boolean) -> Unit) {
    val currentOnNotificationClicked by rememberUpdatedState(onNotificationClick)

    LaunchedEffect(Unit) {
        PendingFcmNotification.pending.collectLatest { data ->
            currentOnNotificationClicked(data.announcementId, data.festivalIdChanged)
        }
    }
}
