package com.daedan.festabook.di

import com.daedan.festabook.data.service.AppVersionService
import com.daedan.festabook.data.service.createAppVersionService
import com.daedan.festabook.delegate.DefaultFirebaseMessagingDelegate
import com.daedan.festabook.delegate.DefaultUserNotificationDelegate
import com.daedan.festabook.domain.repository.DeviceRepository
import com.daedan.festabook.presentation.splash.platform.AppVersionManager
import de.jensklingenberg.ktorfit.Ktorfit
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@DependencyGraph(AppScope::class)
interface IosAppGraph : FestabookAppGraph {
    val appVersionManager: AppVersionManager

    val firebaseMessagingDelegate: DefaultFirebaseMessagingDelegate
    val userNotificationDelegate: DefaultUserNotificationDelegate

    val deviceRepository: DeviceRepository

    @Provides
    fun provideAppVersionService(ktorfit: Ktorfit): AppVersionService = ktorfit.createAppVersionService()

    @Provides
    @Named("Main")
    fun provideMainCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
}
