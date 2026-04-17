package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable

object DeepLinkKeys {
    const val KEY_FESTIVAL_ID = "festivalId"
    const val KEY_ANNOUNCEMENT_ID = "announcementId"
    const val KEY_NOTICE_ID_TO_EXPAND = "noticeIdToExpand"
    const val KEY_CAN_NAVIGATE_TO_NEWS = "canNavigateToNews"
    const val INITIALIZED_ID = -1L
}

/**
 * FCM 알림 클릭 시 호출되는 딥링크 핸들러.
 *
 * @param onNotificationClicked announcementId와 festivalId 변경 여부를 전달.
 *  Android에서는 festivalIdChanged가 항상 false (Activity 재시작으로 처리).
 */
@Composable
expect fun RememberDeepLinkHandler(onNotificationClicked: (announcementId: Long, festivalIdChanged: Boolean) -> Unit)
