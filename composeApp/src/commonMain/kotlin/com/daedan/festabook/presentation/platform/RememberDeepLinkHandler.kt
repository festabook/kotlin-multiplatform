package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable

object DeepLinkKeys {
    const val KEY_FESTIVAL_ID = "festivalId"
    const val KEY_ANNOUNCEMENT_ID = "announcementId"
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

/**
 * @param onFestivalChanged festivalId가 변경되었을 때 호출됨. announcementId를 전달.
 *  iOS에서만 실제로 호출되며, Android에서는 Activity 재시작으로 처리되므로 호출되지 않음.
 * @param onDeepLink 일반 딥링크 처리 콜백 (festivalId 변경 없는 경우)
 */
@Composable
expect fun RememberDeepLinkHandler(
    onFestivalChanged: (announcementId: Long) -> Unit,
    onDeepLink: (Intent) -> Unit,
)
