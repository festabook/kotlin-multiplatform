package com.daedan.festabook.config

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@Inject
@ContributesBinding(AppScope::class)
actual class AppConfig {
    actual val baseUrl: String = ""
}
