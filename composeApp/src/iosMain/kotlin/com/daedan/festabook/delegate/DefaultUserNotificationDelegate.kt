package com.daedan.festabook.delegate

import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.di.coroutine.IO
import com.daedan.festabook.presentation.platform.DeepLinkKeys
import com.daedan.festabook.presentation.platform.FcmDeepLinkAction
import com.daedan.festabook.presentation.platform.FcmMessageType
import com.daedan.festabook.presentation.platform.PendingFcmNotification
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

@Inject
class DefaultUserNotificationDelegate(
    private val festivalLocalDataSource: FestivalLocalDataSource,
    @param:IO private val coroutineScope: CoroutineScope,
) : NSObject(),
    UNUserNotificationCenterDelegateProtocol {
    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        willPresentNotification: UNNotification,
        withCompletionHandler: (UNNotificationPresentationOptions) -> Unit,
    ) {
        val options = UNNotificationPresentationOptionAlert or UNNotificationPresentationOptionSound
        withCompletionHandler(options)
    }

    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        didReceiveNotificationResponse: UNNotificationResponse,
        withCompletionHandler: () -> Unit,
    ) {
        val userInfo = didReceiveNotificationResponse.notification.request.content.userInfo

        val newFestivalId =
            (userInfo[DeepLinkKeys.KEY_FESTIVAL_ID] as? String)?.toLongOrNull()
                ?: DeepLinkKeys.INITIALIZED_ID

        val action =
            userInfo.toDeepLinkAction() ?: run {
                withCompletionHandler()
                return
            }

        coroutineScope.launch {
            val currentFestivalId = festivalLocalDataSource.getFestivalId().firstOrNull()
            val festivalIdChanged =
                newFestivalId != DeepLinkKeys.INITIALIZED_ID && newFestivalId != currentFestivalId

            if (festivalIdChanged) {
                festivalLocalDataSource.saveFestivalId(newFestivalId)
            }

            PendingFcmNotification.store(
                action = action,
                festivalIdChanged = festivalIdChanged,
            )
            withContext(Dispatchers.Main) {
                withCompletionHandler()
            }
        }
    }

    private fun Map<Any?, *>.toDeepLinkAction(): FcmDeepLinkAction? {
        val type = FcmMessageType.from(this[DeepLinkKeys.KEY_TYPE] as? String)
        return when (type) {
            FcmMessageType.WAITING_CALL,
            FcmMessageType.WAITING_ALMOST_CALL,
            -> {
                FcmDeepLinkAction.OpenMyWaiting
            }

            FcmMessageType.WAITING_PLACE_ACCESS_CANCEL -> {
                val placeId =
                    (this[DeepLinkKeys.KEY_PLACE_ID] as? String)?.toLongOrNull() ?: return null
                FcmDeepLinkAction.OpenPlaceDetail(placeId)
            }

            FcmMessageType.ANNOUNCEMENT, null -> {
                val announcementId =
                    (this[DeepLinkKeys.KEY_ANNOUNCEMENT_ID] as? String)?.toLongOrNull()
                        ?: return null
                FcmDeepLinkAction.OpenAnnouncement(announcementId)
            }
        }
    }
}
