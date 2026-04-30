package com.daedan.festabook.logging

import android.os.Build
import android.os.Bundle
import com.daedan.festabook.BuildKonfig
import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.di.coroutine.IO
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale

@Inject
@ContributesBinding(AppScope::class)
class AndroidFirebaseAnalytics(
    @IO scope: CoroutineScope,
    festivalLocalDataSource: FestivalLocalDataSource,
    deviceLocalDataSource: DeviceLocalDataSource,
) : FirebaseAnalytics(scope, festivalLocalDataSource, deviceLocalDataSource) {
    private val platform = Firebase.analytics

    override val initializingJob =
        scope.launch {
            platform.setDefaultEventParameters(
                Bundle().apply {
                    putString("platform", "android")
                    putString("app_version", BuildKonfig.APP_VERSION_NAME)
                    putInt("os_version", Build.VERSION.SDK_INT)
                    putString("device_model", Build.MODEL)
                    putString("language", Locale.getDefault().language)
                    putString("user_id", userId.first { it != null })
                    putLong("timestamp", currentTimeMillis())
                },
            )
        }

    init {
        flushPendingEvents()
    }

    override fun onLogEvent(
        name: String,
        params: Map<String, Any?>?,
    ) {
        platform.logEvent(name) {
            param("festival_id", festivalId.value ?: -1)
            params?.forEach {
                when (val v = it.value) {
                    is Long -> param(it.key, v)
                    is Double -> param(it.key, v)
                    is String -> param(it.key, v)
                    else -> param(it.key, v.toString())
                }
            }
        }
    }
}
