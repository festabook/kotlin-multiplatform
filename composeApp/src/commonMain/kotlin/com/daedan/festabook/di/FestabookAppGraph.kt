package com.daedan.festabook.di

import com.daedan.festabook.logging.FirebaseAnalytics
import com.daedan.festabook.presentation.NotificationPermissionManager
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

interface FestabookAppGraph {
    val metroViewModelFactory: MetroViewModelFactory
    val notificationPermissionManagerFactory: NotificationPermissionManager.Factory

    val analytics: FirebaseAnalytics
}
