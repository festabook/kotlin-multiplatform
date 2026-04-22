package com.daedan.festabook.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import com.daedan.festabook.MainActivity
import com.daedan.festabook.R
import com.daedan.festabook.presentation.platform.DeepLinkKeys
import com.daedan.festabook.presentation.platform.FcmDeepLinkAction
import com.daedan.festabook.presentation.platform.FcmMessageType
import com.daedan.festabook.presentation.theme.FestabookColorPalette
import com.daedan.festabook.vectorToBitmap

object NotificationHelper {
    private const val ANNOUNCEMENT_CHANNEL_ID = "notice_channel"
    private const val ANNOUNCEMENT_CHANNEL_NAME = "공지사항"
    private const val ANNOUNCEMENT_CHANNEL_DESCRIPTION = "앱 공지사항 알림 채널"

    private const val WAITING_CHANNEL_ID = "waiting_channel"
    private const val WAITING_CHANNEL_NAME = "웨이팅"
    private const val WAITING_CHANNEL_DESCRIPTION = "웨이팅 호출 및 상태 알림 채널"

    // Android 8.0에서 알림 채널을 생성
    fun createNotificationChannel(context: Context) {
        val notificationManager =
            context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(
            NotificationChannel(
                ANNOUNCEMENT_CHANNEL_ID,
                ANNOUNCEMENT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH,
            ).apply { description = ANNOUNCEMENT_CHANNEL_DESCRIPTION },
        )
        notificationManager.createNotificationChannel(
            NotificationChannel(
                WAITING_CHANNEL_ID,
                WAITING_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH,
            ).apply { description = WAITING_CHANNEL_DESCRIPTION },
        )
    }

    // 실제 알림을 생성
    fun showNotification(
        context: Context,
        title: String,
        content: String,
        action: FcmDeepLinkAction?,
    ) {
        val intent =
            MainActivity.newIntent(context).apply {
                action?.applyAsExtras(this)
            }

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                System.currentTimeMillis().toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val notificationBuilder =
            NotificationCompat
                .Builder(context, action.toChannelId())
                .setSmallIcon(R.drawable.ic_festabook_logo_notification_small)
                .setColor(FestabookColorPalette().white.toArgb())
                .setContentTitle(title)
                .setContentText(content)
                .setLargeIcon(
                    vectorToBitmap(context, R.drawable.ic_festabook_logo_notification_large),
                ).setStyle(NotificationCompat.BigTextStyle().bigText(content))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    private fun FcmDeepLinkAction.applyAsExtras(intent: Intent) {
        when (this) {
            is FcmDeepLinkAction.OpenAnnouncement -> {
                intent.putExtra(DeepLinkKeys.KEY_TYPE, FcmMessageType.ANNOUNCEMENT.name)
                intent.putExtra(DeepLinkKeys.KEY_CAN_NAVIGATE_TO_NEWS, true)
                intent.putExtra(DeepLinkKeys.KEY_NOTICE_ID_TO_EXPAND, announcementId)
            }

            FcmDeepLinkAction.OpenMyWaiting -> {
                intent.putExtra(DeepLinkKeys.KEY_TYPE, FcmMessageType.WAITING_CALL.name)
            }

            is FcmDeepLinkAction.OpenPlaceDetail -> {
                intent.putExtra(DeepLinkKeys.KEY_TYPE, FcmMessageType.WAITING_PLACE_ACCESS_CANCEL.name)
                intent.putExtra(DeepLinkKeys.KEY_PLACE_ID, placeId)
            }
        }
    }

    private fun FcmDeepLinkAction?.toChannelId(): String =
        when (this) {
            FcmDeepLinkAction.OpenMyWaiting,
            is FcmDeepLinkAction.OpenPlaceDetail,
            -> WAITING_CHANNEL_ID

            is FcmDeepLinkAction.OpenAnnouncement, null -> ANNOUNCEMENT_CHANNEL_ID
        }
}
