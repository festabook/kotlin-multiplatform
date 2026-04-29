package com.daedan.festabook

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.daedan.festabook.di.AndroidAppGraph
import com.daedan.festabook.di.coroutine.IO
import com.daedan.festabook.domain.repository.DeviceRepository
import com.daedan.festabook.presentation.error.FestabookGlobalExceptionHandler
import com.daedan.festabook.service.NotificationHelper
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.naver.maps.map.NaverMapSdk
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.createGraphFactory
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FestabookApp : Application() {
    val androidAppGraph by lazy {
        createGraphFactory<AndroidAppGraph.Factory>().create(this)
    }

    @Inject
    private lateinit var firebaseMessaging: FirebaseMessaging

    @Inject
    private lateinit var deviceRepository: DeviceRepository

    @Inject
    @IO
    private lateinit var applicationScope: CoroutineScope

    override fun onCreate() {
        androidAppGraph.inject(this)
        super.onCreate()
        setupNaverSdk()
        setupNapier()
        sendUnsentReports()
        setupNotificationChannel()
        setupDeviceIdentifiers()
        setLightTheme()
        setGlobalExceptionHandler()
    }

    private fun sendUnsentReports() {
        Firebase.crashlytics.sendUnsentReports()
    }

    private fun setupNotificationChannel() {
        runCatching {
            NotificationHelper.createNotificationChannel(this)
        }.onSuccess {
//            Timber.d("알림 채널 설정 완료")
        }.onFailure { e ->
//            Timber.e(e, "FestabookApp: 알림 채널 설정 실패 ${e.message}")
        }
    }

    private fun setupDeviceIdentifiers() {
        firebaseMessaging
            .token
            .addOnSuccessListener { token ->
                applicationScope.launch {
                    deviceRepository.registerDevice(token)
                    // Timber.d("📡 FCM 토큰 저장: $token")
                }
            }.addOnFailureListener {
                // Timber.w(it, "❌ FCM 토큰 수신 실패")
            }
    }

    private fun setupNaverSdk() {
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NcpKeyClient(BuildKonfig.NAVER_MAP_CLIENT_ID)
    }

    private fun setupNapier() {
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }
    }

    private fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setGlobalExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(FestabookGlobalExceptionHandler(this))
    }
}
