package com.daedan.festabook.delegate

import dev.zacsweers.metro.Inject
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
class DefaultUserNotificationDelegate :
    NSObject(),
    UNUserNotificationCenterDelegateProtocol {
    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        willPresentNotification: UNNotification,
        withCompletionHandler: (UNNotificationPresentationOptions) -> Unit,
    ) {
        // 포그라운드일 때 실행
        val options = UNNotificationPresentationOptionAlert or UNNotificationPresentationOptionSound
        withCompletionHandler(options)
    }

    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        didReceiveNotificationResponse: UNNotificationResponse,
        withCompletionHandler: () -> Unit,
    ) {
        val userInfo = didReceiveNotificationResponse.notification.request.content.userInfo
        val announcementId =
            userInfo["announcementId"] ?: run {
                withCompletionHandler()
                return
            }

        NSNotificationCenter.defaultCenter.postNotificationName(
            aName = "fcmNewsNotification",
            `object` = null,
            userInfo = mapOf("announcementId" to announcementId),
        )

        withCompletionHandler()
    }
}
