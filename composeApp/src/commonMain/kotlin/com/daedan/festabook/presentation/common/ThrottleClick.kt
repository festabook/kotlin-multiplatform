package com.daedan.festabook.presentation.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

fun throttleClick(
    lifecycleOwner: LifecycleOwner,
    onclick: () -> Unit,
) = if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) onclick() else Unit
