package com.daedan.festabook.config

import com.daedan.festabook.BuildConfig
import dev.zacsweers.metro.Inject

@Inject
actual class AppConfig {
    actual val baseUrl: String = BuildConfig.FESTABOOK_URL
}
