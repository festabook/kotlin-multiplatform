package com.daedan.festabook.delegate

import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.presentation.platform.DeepLinkKeys
import com.daedan.festabook.presentation.platform.PendingFcmNotification
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
    private val ioCoroutineScope: CoroutineScope,
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

        val festivalId =
            (userInfo[DeepLinkKeys.KEY_FESTIVAL_ID] as? String)?.toLongOrNull()
                ?: DeepLinkKeys.INITIALIZED_ID

        val announcementId =
            (userInfo[DeepLinkKeys.KEY_ANNOUNCEMENT_ID] as? String)?.toLongOrNull() ?: run {
                withCompletionHandler()
                return
            }

        ioCoroutineScope.launch {
            val currentFestivalId = festivalLocalDataSource.getFestivalId().first()
            val festivalIdChanged =
                festivalId != DeepLinkKeys.INITIALIZED_ID && festivalId != currentFestivalId

            if (festivalId != DeepLinkKeys.INITIALIZED_ID) {
                festivalLocalDataSource.saveFestivalId(festivalId)
            }

            PendingFcmNotification.store(
                announcementId = announcementId,
                festivalIdChanged = festivalIdChanged,
            )
            withContext(Dispatchers.Main) {
                withCompletionHandler()
            }
        }
    }
}
