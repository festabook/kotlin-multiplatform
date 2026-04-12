package com.daedan.festabook.delegate

import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.presentation.platform.DeepLinkKeys
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.Foundation.NSNotificationCenter
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

// 임시
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
            (userInfo[DeepLinkKeys.KEY_ANNOUNCEMENT_ID] as? String) ?: run {
                withCompletionHandler()
                return
            }

        ioCoroutineScope.launch {
            if (festivalId != DeepLinkKeys.INITIALIZED_ID) {
                festivalLocalDataSource.saveFestivalId(festivalId)
            }

            withContext(Dispatchers.Main) {
                NSNotificationCenter.defaultCenter.postNotificationName(
                    aName = DeepLinkKeys.KEY_FCM_NOTIFICATION,
                    `object` = null,
                    userInfo = mapOf(DeepLinkKeys.KEY_ANNOUNCEMENT_ID to announcementId),
                )
                withCompletionHandler()
            }
        }
    }
}
