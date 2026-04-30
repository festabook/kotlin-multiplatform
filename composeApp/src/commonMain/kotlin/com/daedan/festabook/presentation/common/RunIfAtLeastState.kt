package com.daedan.festabook.presentation.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

fun runIfAtLeastState(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.RESUMED,
    action: () -> Unit,
) = if (lifecycleOwner.lifecycle.currentState.isAtLeast(state)) action() else Unit
