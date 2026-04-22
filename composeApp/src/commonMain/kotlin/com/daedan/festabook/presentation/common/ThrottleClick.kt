package com.daedan.festabook.presentation.common

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import kotlin.time.Clock

private const val DEFAULT_CLICK_THROTTLE_MS: Long = 300L

@Composable
fun Modifier.throttleClick(
    windowMs: Long = DEFAULT_CLICK_THROTTLE_MS,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = LocalIndication.current,
    onclick: () -> Unit,
): Modifier {
    var lastClickTime by remember { mutableStateOf(0L) }
    return clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        interactionSource = interactionSource,
        indication = indication,
    ) {
        val now = Clock.System.now().toEpochMilliseconds()
        if (now - lastClickTime >= windowMs) {
            lastClickTime = now
            onclick()
        }
    }
}
