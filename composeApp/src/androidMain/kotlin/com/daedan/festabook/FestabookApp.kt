package com.daedan.festabook

import android.app.Application
import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.FcmDataSource
import com.daedan.festabook.di.AndroidAppGraph
import com.daedan.festabook.presentation.service.NotificationHelper
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.naver.maps.map.NaverMapSdk
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.createGraphFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.UUID

class FestabookApp : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    val festabookAppGraph by lazy {
        createGraphFactory<AndroidAppGraph.Factory>().create(this)
    }

    @Inject
    private lateinit var deviceLocalDataSource: DeviceLocalDataSource

    @Inject
    private lateinit var firebaseMessaging: FirebaseMessaging

    @Inject
    private lateinit var fcmDataSource: FcmDataSource

    override fun onCreate() {
        festabookAppGraph.inject(this)
        super.onCreate()
        setupNaverSdk()
        sendUnsentReports()
        setupNotificationChannel()
        setupDeviceIdentifiers()
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
        applicationScope.launch {
            if (deviceLocalDataSource
                    .getUuid()
                    .firstOrNull()
                    .isNullOrEmpty()
            ) {
                val uuid = UUID.randomUUID().toString()
                deviceLocalDataSource.saveUuid(uuid)
//              Timber.d("🆕 UUID 생성 및 저장: $uuid")
            }

            firebaseMessaging
                .token
                .addOnSuccessListener { token ->
                    applicationScope.launch {
                        fcmDataSource.saveFcmToken(token)
                    }
//                Timber.d("📡 FCM 토큰 저장: $token")
                }.addOnFailureListener {
//                Timber.w(it, "❌ FCM 토큰 수신 실패")
                }
        }
    }

    private fun setupNaverSdk() {
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NcpKeyClient(BuildKonfig.NAVER_MAP_CLIENT_ID)
    }
}
