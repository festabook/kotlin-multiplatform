package com.daedan.festabook.presentation

expect class NotificationPermissionManager {
    interface Factory

    suspend fun checkPermission(): PermissionState

    fun requestPermission()
}
