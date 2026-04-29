package com.daedan.festabook.logging

import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class FirebaseAnalytics(
    private val scope: CoroutineScope,
    festivalLocalDataSource: FestivalLocalDataSource,
    deviceLocalDataSource: DeviceLocalDataSource,
) {
    val festivalId =
        festivalLocalDataSource
            .getFestivalId()
            .stateIn(scope, SharingStarted.Eagerly, null)

    val userId =
        deviceLocalDataSource
            .getUuid()

    private val pendingEvent = Channel<PendingEvent>(Channel.UNLIMITED)

    abstract val initializingJob: Job

    fun flushPendingEvents() {
        scope.launch {
            initializingJob.join()
            for (event in pendingEvent) {
                logEvent(event.name, event.params)
            }
        }
    }

    fun logEvent(
        name: String,
        params: Map<String, Any?>? = null,
    ) {
        if (!initializingJob.isCompleted) {
            pendingEvent.trySend(PendingEvent(name, params))
            return
        }
        onLogEvent(name, params)
    }

    abstract fun onLogEvent(
        name: String,
        params: Map<String, Any?>?,
    )

    protected data class PendingEvent(
        val name: String,
        val params: Map<String, Any?>?,
    )
}
