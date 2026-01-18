package com.daedan.festabook.config

import dev.zacsweers.metro.Inject

@Inject
expect class AppConfig {
    val baseUrl: String
}
