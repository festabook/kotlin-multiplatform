package com.daedan.festabook.delegate

import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

// 임시
class DefaultUserNotificationDelegate :
    NSObject(),
    UNUserNotificationCenterDelegateProtocol {
    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        willPresentNotification: UNNotification,
        withCompletionHandler: (UNNotificationPresentationOptions) -> Unit,
    ) {
        // 포그라운드일 때 실행
        val userInfo = willPresentNotification.request.content.userInfo
        val options = UNNotificationPresentationOptionAlert or UNNotificationPresentationOptionSound
        withCompletionHandler(options)
    }
}
