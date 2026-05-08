package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable

object DeepLinkKeys {
    const val KEY_FESTIVAL_ID = "festivalId"
    const val KEY_ANNOUNCEMENT_ID = "announcementId"
    const val KEY_NOTICE_ID_TO_EXPAND = "noticeIdToExpand"
    const val KEY_CAN_NAVIGATE_TO_NEWS = "canNavigateToNews"
    const val KEY_TYPE = "type"
    const val KEY_PLACE_ID = "placeId"
    const val INITIALIZED_ID = -1L

    const val KEY_NOTIFICATION_SENT_AT = "notificationSentAt"
}

enum class FcmMessageType {
    ANNOUNCEMENT,
    WAITING_CALL,
    WAITING_ALMOST_CALL,
    WAITING_PLACE_ACCESS_CANCEL,
    ;

    companion object {
        fun from(raw: String?): FcmMessageType? = entries.firstOrNull { it.name == raw }
    }
}

sealed interface FcmDeepLinkAction {
    data class OpenAnnouncement(
        val announcementId: Long,
        val notificationSentAt: Long,
    ) : FcmDeepLinkAction

    data object OpenMyWaiting : FcmDeepLinkAction

    data class OpenPlaceDetail(
        val placeId: Long,
    ) : FcmDeepLinkAction
}

/**
 * FCM 알림 클릭 시 호출되는 딥링크 핸들러.
 *
 * @param onNotificationClick 분기된 [FcmDeepLinkAction]과 festivalId 변경 여부를 전달.
 *  Android에서는 festivalIdChanged가 항상 false (Activity 재시작으로 처리).
 */
@Composable
expect fun RememberDeepLinkHandler(onNotificationClick: (action: FcmDeepLinkAction, festivalIdChanged: Boolean) -> Unit)
