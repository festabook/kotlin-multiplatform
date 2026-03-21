package com.daedan.festabook.logging

import cocoapods.FirebaseCrashlytics.FIRCrashlytics
import com.daedan.festabook.BuildKonfig
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError
import platform.Foundation.NSLocalizedDescriptionKey

@OptIn(ExperimentalForeignApi::class)
actual object FirebaseCrashlytics {
    private val platform = FIRCrashlytics.crashlytics()

    actual fun recordException(throwable: Throwable) {
        platform.recordError(throwable.asNSError())
    }

    actual fun sendUnsentReports() {
        platform.sendUnsentReports()
    }
}

fun Throwable.asNSError(): NSError {
    val userInfo = mutableMapOf<Any?, Any?>()

    message?.let {
        userInfo[NSLocalizedDescriptionKey] = it
    }

    userInfo["KotlinStackTrace"] = stackTraceToString()
    return NSError(
        domain = BuildKonfig.APP_BUNDLE_ID,
        code = 0,
        userInfo = userInfo,
    )
}
