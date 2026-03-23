package com.daedan.festabook.logging

import cocoapods.FirebaseAnalytics.FIRAnalytics
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine

@OptIn(ExperimentalForeignApi::class)
actual object FirebaseAnalytics {
    private val platform = FIRAnalytics
    private const val KEY_UNINITIALIZED_USER_ID = "undefined"

    @Suppress("UNCHECKED_CAST")
    actual fun logEvent(
        name: String,
        params: Map<String, Any?>?,
    ) {
        platform.logEventWithName(
            name,
            params as? Map<Any?, Any?>,
        )
    }

    actual suspend fun getAppInstanceId(): String = platform.appInstanceID() ?: KEY_UNINITIALIZED_USER_ID

    actual suspend fun getSessionId(): Long =
        suspendCancellableCoroutine { cont ->
            platform.sessionIDWithCompletion { id, error ->
                if (error != null) {
                    cont.resumeWith(Result.failure(RuntimeException(error.localizedDescription)))
                } else {
                    cont.resumeWith(Result.success(id))
                }
            }
        }
}
