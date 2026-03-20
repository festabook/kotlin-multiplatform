package com.daedan.festabook.presentation

import androidx.activity.ComponentActivity
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
actual class ContextFactory(
    @Assisted private val activity: ComponentActivity,
) {
    @AssistedFactory
    interface Factory {
        fun create(activity: ComponentActivity): ContextFactory
    }

    actual fun getContext(): Any = activity.baseContext

    actual fun getApplication(): Any = activity.application

    actual fun getActivity(): Any = activity
}
