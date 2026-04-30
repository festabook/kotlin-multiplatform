package com.daedan.festabook.logging

import androidx.compose.runtime.Composable
import com.daedan.festabook.presentation.common.rememberAnalytics

@Composable
fun logClick(
    identifier: String,
    screenName: String,
    onClick: () -> Unit,
): () -> Unit {
    val analytics = rememberAnalytics()
    return {
        analytics.logEvent("button_click", mapOf("button_id" to identifier, "screen" to screenName))
        onClick()
    }
}
