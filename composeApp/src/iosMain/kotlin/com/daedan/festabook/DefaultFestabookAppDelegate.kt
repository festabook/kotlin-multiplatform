package com.daedan.festabook

import cocoapods.FirebaseCore.FIRApp
import cocoapods.FirebaseMessaging.FIRMessaging
import cocoapods.NMapsMap.NMFAuthManager
import com.daedan.festabook.delegate.DefaultFirebaseMessagingDelegate
import com.daedan.festabook.delegate.DefaultUserNotificationDelegate
import com.daedan.festabook.delegate.FestabookAppDelegate
import com.daedan.festabook.di.IosAppGraph
import com.daedan.festabook.logging.FirebaseCrashlytics
import dev.zacsweers.metro.createGraph
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import platform.Foundation.NSData
import platform.Foundation.NSException
import platform.Foundation.NSSetUncaughtExceptionHandler
import platform.UIKit.UIApplication
import platform.UIKit.UIBackgroundFetchResult
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIWindow
import platform.UserNotifications.UNUserNotificationCenter

@OptIn(ExperimentalForeignApi::class)
object DefaultFestabookAppDelegate : FestabookAppDelegate {
    override val appGraph: IosAppGraph = createGraph()

    private val firebaseMessagingDelegate: DefaultFirebaseMessagingDelegate by lazy {
        appGraph.firebaseMessagingDelegate
    }
    private val userNotificationDelegate: DefaultUserNotificationDelegate by lazy {
        appGraph.userNotificationDelegate
    }

    // 앱 최초 실행
    override fun application(
        application: UIApplication,
        launchOptions: Map<Any?, *>?,
    ): Boolean {
        setGlobalExceptionHandler()
        FIRApp.configure()
        setupNapier()
        NMFAuthManager.shared().ncpKeyId = BuildKonfig.NAVER_MAP_CLIENT_ID
        FIRMessaging.messaging().delegate = firebaseMessagingDelegate
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.delegate = userNotificationDelegate
        setLightTheme(application)
        return true
    }

    override fun application(
        application: UIApplication,
        deviceToken: NSData,
    ) {
        FIRMessaging.messaging().APNSToken = deviceToken
        firebaseMessagingDelegate.registerFcmToken()
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

    private fun setupNapier() {
        if (BuildKonfig.BUILD_FLAVOR == "dev") {
            Napier.base(DebugAntilog())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setLightTheme(application: UIApplication) {
        UIWindow.appearance().overrideUserInterfaceStyle =
            UIUserInterfaceStyle.UIUserInterfaceStyleLight
    }

    private fun setGlobalExceptionHandler() {
        NSSetUncaughtExceptionHandler(
            staticCFunction { exception: NSException? ->
                val message =
                    "${exception?.name ?: "Unknown"}: ${exception?.reason ?: "Unknown error"}"
                FirebaseCrashlytics.recordException(Exception(message))
            },
        )
    }
}
