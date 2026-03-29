package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable

object DeepLinkKeys {
    const val KEY_NOTICE_ID_TO_EXPAND = "noticeIdToExpand"
    const val KEY_CAN_NAVIGATE_TO_NEWS = "canNavigateToNews"
    const val INITIALIZED_ID = -1L
}

expect class Intent {
    fun getLongExtra(
        key: String,
        defaultValue: Long,
    ): Long

    fun getBooleanExtra(
        key: String,
        defaultValue: Boolean,
    ): Boolean
}

@Composable
expect fun RememberDeepLinkHandler(onDeepLink: (Intent) -> Unit)
