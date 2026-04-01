package com.daedan.festabook.di

import android.app.Application
import android.content.Context
import com.daedan.festabook.FestabookApp
import com.daedan.festabook.presentation.splash.platform.AppVersionManager
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides

@DependencyGraph(AppScope::class)
interface AndroidAppGraph : FestabookAppGraph {
    val appVersionManagerFactory: AppVersionManager.Factory

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides application: Application,
        ): AndroidAppGraph
    }

    fun inject(app: FestabookApp)

    fun inject(firebaseMessagingService: FirebaseMessagingService)

    @Provides
    fun provideAppUpdateManager(application: Application): AppUpdateManager = AppUpdateManagerFactory.create(application)

    @Provides
    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()
}

val Context.androidAppGraph get() = (applicationContext as FestabookApp).androidAppGraph
