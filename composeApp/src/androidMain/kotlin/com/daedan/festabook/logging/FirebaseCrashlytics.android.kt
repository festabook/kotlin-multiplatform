package com.daedan.festabook.logging

import com.google.firebase.crashlytics.FirebaseCrashlytics as PlatformFirebaseCrashlytics

actual object FirebaseCrashlytics {
    private val platform: PlatformFirebaseCrashlytics = PlatformFirebaseCrashlytics.getInstance()

    actual fun recordException(throwable: Throwable) {
        platform.recordException(throwable)
    }

    actual fun sendUnsentReports() {
        platform.sendUnsentReports()
    }
}
