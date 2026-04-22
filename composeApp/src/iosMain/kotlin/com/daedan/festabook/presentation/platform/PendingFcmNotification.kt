package com.daedan.festabook.presentation.platform

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * iOS에서 FCM 알림 클릭 시 딥링크 데이터를 전달하는 Channel 기반 버퍼.
 *
 * NSNotification은 fire-and-forget이라 Compose UI가 준비되기 전(cold start)에
 * 발사되면 유실됨. Channel은 collector가 소비할 때까지 데이터를 보관하므로:
 * - Cold start: 데이터가 버퍼에 대기 → Compose 준비 후 collect 시 즉시 수신
 * - 앱 실행 중: 데이터 전송 → 이미 collect 중인 handler가 즉시 수신
 */
object PendingFcmNotification {
    private val _pending = Channel<Data>(capacity = Channel.BUFFERED)
    val pending: Flow<Data> = _pending.receiveAsFlow()

    fun store(
        action: FcmDeepLinkAction,
        festivalIdChanged: Boolean,
    ) {
        _pending.trySend(
            Data(
                action = action,
                festivalIdChanged = festivalIdChanged,
            ),
        )
    }

    data class Data(
        val action: FcmDeepLinkAction,
        val festivalIdChanged: Boolean,
    )
}
