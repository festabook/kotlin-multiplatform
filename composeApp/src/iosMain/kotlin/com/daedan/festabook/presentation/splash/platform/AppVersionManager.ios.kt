package com.daedan.festabook.presentation.splash.platform

import com.daedan.festabook.domain.repository.AppVersionRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.Foundation.NSBundle
import platform.Foundation.NSLog
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.posix.exit

@Inject
actual class AppVersionManager(
    private val appVersionRepository: AppVersionRepository,
) {
    private val currentAppVersion =
        NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String

    private val appleId = "6752591661"

    private val appStoreOpenUrl = "itms-apps://itunes.apple.com/app/apple-store/id/$appleId"

    actual suspend fun getIsAppUpdateAvailable(): Result<Boolean> {
        val latestVersion = appVersionRepository.getLatestVersion()
        return latestVersion.map {
            it != currentAppVersion
        }
    }

    actual fun updateApp() {
        val url = NSURL.URLWithString(appStoreOpenUrl)

        if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(
                url = url,
                options = emptyMap<Any?, Any>(),
                completionHandler = null,
            )
        } else {
            NSLog("[AppStoreCheck] 앱스토어 URL을 열 수 없습니다")
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(500) // 0.5초 대기
            exit(0)
        }
    }
}
