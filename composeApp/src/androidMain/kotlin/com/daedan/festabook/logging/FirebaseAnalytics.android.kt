package com.daedan.festabook.logging

import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.tasks.await

actual object FirebaseAnalytics {
    private val platform = Firebase.analytics

    actual fun logEvent(
        name: String,
        params: Map<String, Any?>?,
    ) {
        platform.logEvent(name) {
            params?.forEach {
                param(it.key, it.value.toString())
            }
        }
    }

    actual suspend fun getAppInstanceId(): String = platform.appInstanceId.await()

    actual suspend fun getSessionId(): Long = platform.sessionId.await()
}
