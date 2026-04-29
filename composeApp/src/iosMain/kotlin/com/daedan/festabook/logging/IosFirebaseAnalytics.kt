package com.daedan.festabook.logging

import cocoapods.FirebaseAnalytics.FIRAnalytics
import com.daedan.festabook.BuildKonfig
import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import platform.Foundation.NSDate
import platform.Foundation.NSLocale
import platform.Foundation.NSProcessInfo
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.currentLocale
import platform.Foundation.date
import platform.Foundation.languageCode
import platform.Foundation.stringWithCString
import platform.Foundation.timeIntervalSince1970
import platform.UIKit.UIDevice
import platform.posix.uname
import platform.posix.utsname

@Inject
@ContributesBinding(AppScope::class)
@OptIn(ExperimentalForeignApi::class)
class IosFirebaseAnalytics(
    scope: CoroutineScope,
    festivalLocalDataSource: FestivalLocalDataSource,
    deviceLocalDataSource: DeviceLocalDataSource,
) : FirebaseAnalytics(scope, festivalLocalDataSource, deviceLocalDataSource) {
    private val platform = FIRAnalytics

    override val initializingJob: Job =
        scope.launch {
            platform.setDefaultEventParameters(
                mapOf(
                    "platform" to "ios",
                    "app_version" to BuildKonfig.APP_VERSION_NAME,
                    "os_version" to NSProcessInfo.processInfo.operatingSystemVersionString,
                    "device_model" to getDeviceModel(),
                    "language" to NSLocale.currentLocale.languageCode,
                    "user_id" to userId.first { it != null },
                    "timestamp" to currentTimeMillis(),
                ),
            )
        }

    init {
        flushPendingEvents()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onLogEvent(
        name: String,
        params: Map<String, Any?>?,
    ) {
        val mergedParams =
            buildMap {
                params?.let { putAll(it) }
                put("festival_id", festivalId.value ?: -1)
            } as? Map<Any?, Any?>

        platform.logEventWithName(name, mergedParams)
    }

    private fun getDeviceModel(): String {
        memScoped {
            val systemInfo = alloc<utsname>()
            uname(systemInfo.ptr)
            return NSString.stringWithCString(
                systemInfo.machine,
                encoding = NSUTF8StringEncoding,
            ) ?: UIDevice.currentDevice.model
        }
    }

    private fun currentTimeMillis(): Long = (NSDate.date().timeIntervalSince1970 * 1000).toLong()
}
