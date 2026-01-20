package com.daedan.festabook.config

interface AppConfig {
    val baseUrl: String
}

expect fun createAppConfig(): AppConfig
