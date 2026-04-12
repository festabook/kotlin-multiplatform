package com.daedan.festabook.service

import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.di.androidAppGraph
import com.daedan.festabook.presentation.platform.DeepLinkKeys
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
        if (remoteMessage.data.isNotEmpty()) {
            ioCoroutineScope.launch {
                val title =
                    remoteMessage.data["title"] ?: getString(Res.string.default_notification_title)
                val content =
                    remoteMessage.data["body"] ?: getString(Res.string.default_notification_body)
                val noticeIdToExpand =
                    remoteMessage.data["announcementId"]?.toLongOrNull()
                        ?: DeepLinkKeys.INITIALIZED_ID
                val festivalId =
                    remoteMessage.data["festivalId"]?.toLongOrNull() ?: DeepLinkKeys.INITIALIZED_ID

                handleMessageData(festivalId, title, content, noticeIdToExpand)
            }
        }
    }

    private suspend fun handleMessageData(
        festivalId: Long,
        title: String,
        content: String,
        noticeIdToExpand: Long,
    ) {
        if (festivalId != DeepLinkKeys.INITIALIZED_ID) {
            festivalLocalDataSource.saveFestivalId(festivalId)
        }

        NotificationHelper.showNotification(
            context = applicationContext,
            title = title,
            content = content,
            announcementId = noticeIdToExpand,
        )
    }
}
