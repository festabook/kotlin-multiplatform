package com.daedan.festabook

import cocoapods.FirebaseCore.FIRApp
import cocoapods.FirebaseMessaging.FIRMessaging
import cocoapods.NMapsMap.NMFAuthManager
import com.daedan.festabook.delegate.DefaultFirebaseMessagingDelegate
import com.daedan.festabook.delegate.DefaultUserNotificationDelegate
import com.daedan.festabook.delegate.FestabookAppDelegate
import com.daedan.festabook.di.IosAppGraph
import dev.zacsweers.metro.createGraph
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import platform.Foundation.NSData
import platform.UIKit.UIApplication
import platform.UIKit.UIBackgroundFetchResult
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalForeignApi::class, ExperimentalObjCName::class)
object DefaultFestabookAppDelegate : FestabookAppDelegate {
    override val appGraph: IosAppGraph = createGraph()
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val firebaseMessagingDelegate by lazy {
        DefaultFirebaseMessagingDelegate(appScope)
    }

    private val userNotificationDelegate by lazy {
        DefaultUserNotificationDelegate()
    }

    override fun application(
        application: UIApplication,
        launchOptions: Map<Any?, *>?,
    ): Boolean {
        FIRApp.configure()
        NMFAuthManager.shared().ncpKeyId = BuildKonfig.NAVER_MAP_CLIENT_ID
        FIRMessaging.messaging().delegate = firebaseMessagingDelegate
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.delegate = userNotificationDelegate
        return true
    }

    override fun application(
        application: UIApplication,
        deviceToken: NSData,
    ) {
        FIRMessaging.messaging().APNSToken = deviceToken
    }

    // 백그라운드에서 실행
    override fun application(
        application: UIApplication,
        userInfo: Map<Any?, *>,
        fetchCompletionHandler: (UIBackgroundFetchResult) -> Unit,
    ) {
        // 이후 화면 이동 로직 처리
        fetchCompletionHandler(UIBackgroundFetchResult.UIBackgroundFetchResultNewData)
    }
}
