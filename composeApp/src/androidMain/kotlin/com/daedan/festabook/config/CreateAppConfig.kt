package com.daedan.festabook.config

import com.daedan.festabook.BuildConfig

actual fun createAppConfig(): AppConfig =
    object : AppConfig {
        override val baseUrl: String = BuildConfig.FESTABOOK_URL
    }
