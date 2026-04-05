package com.daedan.festabook.service

import com.daedan.festabook.R
import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.di.androidAppGraph
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val festivalLocalDataSource: FestivalLocalDataSource by lazy {
        application.androidAppGraph.festivalLocalDataSource
    }

    override fun onNewToken(token: String) {
//        Timber.d("Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        Timber.d("From: ${remoteMessage.from}")

        // remoteMessage.data
        if (remoteMessage.data.isNotEmpty()) {
//            Timber.d("Data Payload: ${remoteMessage.data}")
            val title =
                remoteMessage.data["title"] ?: getString(R.string.default_notification_title)
            val content =
                remoteMessage.data["body"] ?: getString(R.string.default_notification_body)
            val noticeIdToExpand = remoteMessage.data["announcementId"] ?: "-1"
            val festivalId = remoteMessage.data["festivalId"] ?: "-1"

            handleMessageData(festivalId, title, content, noticeIdToExpand)
        }
    }

    private fun handleMessageData(
        festivalId: String,
        title: String,
        content: String,
        noticeIdToExpand: String,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            festivalId.toLongOrNull()?.let { id ->
                festivalLocalDataSource.saveFestivalId(id)
            }

            NotificationHelper.showNotification(
                context = applicationContext,
                title = title,
                content = content,
                announcementId = noticeIdToExpand,
            )
        }
    }
}
