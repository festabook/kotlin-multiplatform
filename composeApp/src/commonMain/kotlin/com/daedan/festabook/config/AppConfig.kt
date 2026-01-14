package com.daedan.festabook.config

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@Inject
@ContributesBinding(AppScope::class)
expect class AppConfig {
    val baseUrl: String
}
