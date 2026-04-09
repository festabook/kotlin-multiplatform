package com.daedan.festabook.delegate

import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Named
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.Foundation.NSDate
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSURL
import platform.Foundation.timeIntervalSince1970
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationAttachment
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

// 임시
@Inject
class DefaultUserNotificationDelegate(
    private val festivalLocalDataSource: FestivalLocalDataSource,
    @param:Named("IO") private val scope: CoroutineScope,
) : NSObject(),
    UNUserNotificationCenterDelegateProtocol {
    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        willPresentNotification: UNNotification,
        withCompletionHandler: (UNNotificationPresentationOptions) -> Unit,
    ) {
        val identifier = willPresentNotification.request.identifier
        val userInfo = willPresentNotification.request.content.userInfo
        val content = willPresentNotification.request.content

        if (identifier.startsWith("festabook_local")) {
            val options =
                UNNotificationPresentationOptionAlert or UNNotificationPresentationOptionSound
            withCompletionHandler(options)
            return
        }

        val title = content.title.ifBlank { "기본 제목" }
        val body = content.body.ifBlank { "기본 내용" }
        val festivalId = userInfo["festivalId"] as? String ?: "-1"
        val announcementId = userInfo["announcementId"] as? String ?: "-1"
        val imagePath = ""

        showLocalNotificationWithImage(
            title = title,
            body = body,
            festivalId = festivalId,
            announcementId = announcementId,
            imagePath = imagePath,
        )
        withCompletionHandler(0u)
    }

    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        didReceiveNotificationResponse: UNNotificationResponse,
        withCompletionHandler: () -> Unit,
    ) {
        val userInfo = didReceiveNotificationResponse.notification.request.content.userInfo
        val festivalId = userInfo["festivalId"] as? String ?: "-1"
        val announcementId =
            userInfo["announcementId"] ?: run {
                withCompletionHandler()
                return
            }

        scope.launch {
            festivalId.toLongOrNull()?.let { id ->
                festivalLocalDataSource.saveFestivalId(id)
            }

            NSNotificationCenter.defaultCenter.postNotificationName(
                aName = "fcmNewsNotification",
                `object` = null,
                userInfo = mapOf("announcementId" to announcementId),
            )
        }

        withCompletionHandler()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun showLocalNotificationWithImage(
        title: String,
        body: String,
        festivalId: String,
        announcementId: String,
        imagePath: String?,
    ) {
        val content =
            UNMutableNotificationContent().apply {
                setTitle(title)
                setBody(body)
                setUserInfo(mapOf("festivalId" to festivalId, "announcementId" to announcementId))

                imagePath?.let { path ->
                    val fileUrl = NSURL.fileURLWithPath(path)
                    val attachment =
                        UNNotificationAttachment.attachmentWithIdentifier(
                            identifier = "image_attach_${NSDate().timeIntervalSince1970}",
                            URL = fileUrl,
                            options = null,
                            error = null,
                        )
                    if (attachment != null) {
                        setAttachments(listOf(attachment))
                    }
                }
            }

        val request =
            UNNotificationRequest.requestWithIdentifier(
                identifier = "festabook_local_${NSDate().timeIntervalSince1970}",
                content = content,
                trigger = null,
            )

        UNUserNotificationCenter
            .currentNotificationCenter()
            .addNotificationRequest(request) { error ->
                if (error != null) Napier.e("fcm 에러: $error")
            }
    }

    companion object {
        private const val festivalId = "festivalId"
        private const val announcementId = ""
    }
}
