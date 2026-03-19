package com.daedan.festabook.presentation.splash.platform

expect class AppVersionManager {
    suspend fun getIsAppUpdateAvailable(): Result<Boolean>

    fun updateApp()
}
