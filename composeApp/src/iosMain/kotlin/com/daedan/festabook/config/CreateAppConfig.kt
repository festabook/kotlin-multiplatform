package com.daedan.festabook.config

import platform.Foundation.NSBundle

actual fun createAppConfig(): AppConfig =
    object : AppConfig {
        override val baseUrl: String =
            NSBundle.mainBundle.objectForInfoDictionaryKey("BASE_URL") as? String
                ?: error("Config 파일에 BASE_URL 없음")
    }
