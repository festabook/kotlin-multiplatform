package com.daedan.festabook.logging

expect object FirebaseCrashlytics {
    fun recordException(throwable: Throwable)

    fun sendUnsentReports()
}
