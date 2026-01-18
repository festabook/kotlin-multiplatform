package com.daedan.festabook.config

import dev.zacsweers.metro.Inject
import platform.Foundation.NSBundle

@Inject
actual class AppConfig {
    actual val baseUrl: String =
        NSBundle.mainBundle.objectForInfoDictionaryKey("BASE_URL") as? String
            ?: error("Config 파일에 BASE_URL 없음")
}
