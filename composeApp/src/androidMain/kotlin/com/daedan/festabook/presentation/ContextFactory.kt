package com.daedan.festabook.presentation

import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
actual class ContextFactory(
    @Assisted private val componentActivity: ComponentActivity,
) {
    @AssistedFactory
    interface Factory {
        fun create(activity: ComponentActivity): ContextFactory
    }

    val activityContext: Context get() = createActivityContext() as Context

    val application: Application = createApplicationContext() as Application

    val activity: ComponentActivity get() = createActivity() as ComponentActivity

    actual fun createActivityContext(): Any = componentActivity.baseContext

    actual fun createApplicationContext(): Any = componentActivity.application

    actual fun createActivity(): Any = componentActivity
}
