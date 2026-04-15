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
actual fun RememberDeepLinkHandler(
    onFestivalChanged: (announcementId: Long) -> Unit,
    onDeepLink: (Intent) -> Unit,
) {
    val currentOnFestivalChanged by rememberUpdatedState(onFestivalChanged)
    val currentOnDeepLink by rememberUpdatedState(onDeepLink)

    LaunchedEffect(Unit) {
        PendingFcmNotification.pending.collectLatest { data ->
            if (data.festivalIdChanged) {
                currentOnFestivalChanged(data.announcementId)
            } else {
                currentOnDeepLink(
                    Intent(
                        mapOf(
                            DeepLinkKeys.KEY_NOTICE_ID_TO_EXPAND to data.announcementId,
                            DeepLinkKeys.KEY_CAN_NAVIGATE_TO_NEWS to true,
                        ),
                    ),
                )
            }
        }
    }
}
