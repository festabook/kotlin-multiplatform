package com.daedan.festabook.service

import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.di.androidAppGraph
import com.daedan.festabook.presentation.platform.DeepLinkKeys
import com.daedan.festabook.presentation.platform.FcmDeepLinkAction
import com.daedan.festabook.presentation.platform.FcmMessageType
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.default_notification_body
import festabookkmp.composeapp.generated.resources.default_notification_title
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val festivalLocalDataSource: FestivalLocalDataSource by lazy {
        application.androidAppGraph.festivalLocalDataSource
    }

    private val ioCoroutineScope: CoroutineScope by lazy {
        application.androidAppGraph.ioCoroutineScope
    }

    override fun onNewToken(token: String) {
//        Timber.d("Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isEmpty()) return

        ioCoroutineScope.launch {
            val title =
                remoteMessage.data["title"] ?: getString(Res.string.default_notification_title)
            val content =
                remoteMessage.data["body"] ?: getString(Res.string.default_notification_body)
            val festivalId =
                remoteMessage.data[DeepLinkKeys.KEY_FESTIVAL_ID]?.toLongOrNull()
                    ?: DeepLinkKeys.INITIALIZED_ID
            val action = remoteMessage.data.toDeepLinkAction()

            handleMessageData(
                festivalId = festivalId,
                title = title,
                content = content,
                action = action,
            )
        }
    }

    private fun Map<String, String>.toDeepLinkAction(): FcmDeepLinkAction? {
        val type = FcmMessageType.from(this[DeepLinkKeys.KEY_TYPE])
        return when (type) {
            FcmMessageType.WAITING_CALL,
            FcmMessageType.WAITING_ALMOST_CALL,
            -> {
                FcmDeepLinkAction.OpenMyWaiting
            }

            FcmMessageType.WAITING_PLACE_ACCESS_CANCEL -> {
                val placeId = this[DeepLinkKeys.KEY_PLACE_ID]?.toLongOrNull() ?: return null
                FcmDeepLinkAction.OpenPlaceDetail(placeId)
            }

            FcmMessageType.ANNOUNCEMENT, null -> {
                val announcementId =
                    this[DeepLinkKeys.KEY_ANNOUNCEMENT_ID]?.toLongOrNull() ?: return null
                FcmDeepLinkAction.OpenAnnouncement(announcementId)
            }
        }
    }

    private suspend fun handleMessageData(
        festivalId: Long,
        title: String,
        content: String,
        action: FcmDeepLinkAction?,
    ) {
        if (festivalId != DeepLinkKeys.INITIALIZED_ID) {
            festivalLocalDataSource.saveFestivalId(festivalId)
        }

        NotificationHelper.showNotification(
            context = applicationContext,
            title = title,
            content = content,
            action = action,
        )
    }
}
