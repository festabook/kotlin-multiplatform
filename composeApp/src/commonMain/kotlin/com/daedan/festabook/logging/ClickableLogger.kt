package com.daedan.festabook.logging

import androidx.compose.runtime.Composable
import com.daedan.festabook.presentation.common.rememberAnalytics

@Composable
fun logClick(
    identifier: String,
    screenName: String,
    extraParam: Map<String, String> = emptyMap(),
    onClick: () -> Unit,
): () -> Unit {
    val analytics = rememberAnalytics()
    return {
        analytics.logEvent(
            "button_click",
            buildMap {
                put("button_id", identifier)
                put("screen", screenName)
                putAll(extraParam)
            },
        )

        onClick()
    }
}
