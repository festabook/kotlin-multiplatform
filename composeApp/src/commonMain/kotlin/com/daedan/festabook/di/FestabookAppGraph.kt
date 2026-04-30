package com.daedan.festabook.di

import com.daedan.festabook.di.coroutine.IO
import com.daedan.festabook.logging.FirebaseAnalytics
import com.daedan.festabook.presentation.NotificationPermissionManager
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import kotlinx.coroutines.CoroutineScope

interface FestabookAppGraph {
    val metroViewModelFactory: MetroViewModelFactory
    val notificationPermissionManagerFactory: NotificationPermissionManager.Factory

    val analytics: FirebaseAnalytics


    @IO
    val coroutineScope: CoroutineScope
}
