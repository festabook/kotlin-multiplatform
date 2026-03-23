package com.daedan.festabook.logging

expect object FirebaseAnalytics {
    fun logEvent(
        name: String,
        params: Map<String, Any?>? = null,
    )

    suspend fun getAppInstanceId(): String

    suspend fun getSessionId(): Long
}
