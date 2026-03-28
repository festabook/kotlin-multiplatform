package com.daedan.festabook.presentation.splash.platform

import com.daedan.festabook.BuildKonfig
import com.daedan.festabook.domain.repository.AppVersionRepository
import dev.zacsweers.metro.Inject
import platform.Foundation.NSBundle
import platform.Foundation.NSLog
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Inject
actual class AppVersionManager(
    private val appVersionRepository: AppVersionRepository,
) {
    private val currentAppVersion =
        NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String

    actual suspend fun getIsAppUpdateAvailable(): Result<Boolean> {
        if (BuildKonfig.BUILD_FLAVOR == "dev") return Result.success(false)
        val latestVersion = appVersionRepository.getLatestVersion(BuildKonfig.APP_BUNDLE_ID)
        return latestVersion.map { latest ->
            isNewerVersion(latest, currentAppVersion)
        }
    }

    private fun isNewerVersion(
        latest: String,
        current: String?,
    ): Boolean {
        if (current == null) return true
        val latestParts = latest.split(".").mapNotNull { it.toIntOrNull() }
        val currentParts = current.split(".").mapNotNull { it.toIntOrNull() }
        val maxLength = maxOf(latestParts.size, currentParts.size)
        for (i in 0 until maxLength) {
            val l = latestParts.getOrElse(i) { 0 }
            val c = currentParts.getOrElse(i) { 0 }
            if (l > c) return true
            if (l < c) return false
        }
        return false
    }

    actual fun updateApp() {
        listOfNotNull(
            APP_STORE_URL.toUrl(),
            APP_STORE_WEB_URL.toUrl(),
        ).firstOrNull()?.let {
            UIApplication.sharedApplication.openURL(
                url = it,
                options = emptyMap<Any?, Any>(),
                completionHandler = null,
            )
        } ?: NSLog("[AppStoreCheck] 앱 스토어를 열 수 없습니다")
    }

    private fun String.toUrl(): NSURL? {
        val url = NSURL.URLWithString(this) ?: return null
        if (!UIApplication.sharedApplication.canOpenURL(url)) return null
        return url
    }

    companion object {
        private const val APPLE_ID = "6752591661"
        private const val APP_STORE_URL =
            "itms-apps://itunes.apple.com/app/apple-store/id/$APPLE_ID"
        private const val APP_STORE_WEB_URL = "https://apps.apple.com/app/id/$APPLE_ID"
    }
}
