package com.daedan.festabook.presentation.platform

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.util.Consumer

actual typealias Intent = Intent

@Composable
actual fun RememberDeepLinkHandler(onDeepLink: (Intent) -> Unit) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity ?: return
    val currentOnDeepLink by rememberUpdatedState(onDeepLink)

    LaunchedEffect(Unit) {
        currentOnDeepLink(activity.intent)
        activity.intent.removeExtra(DeepLinkKeys.KEY_CAN_NAVIGATE_TO_NEWS)
        activity.intent.removeExtra(DeepLinkKeys.KEY_NOTICE_ID_TO_EXPAND)
    }

    DisposableEffect(activity) {
        val listener =
            Consumer<Intent> { intent ->
                currentOnDeepLink(intent)
                intent.removeExtra(DeepLinkKeys.KEY_CAN_NAVIGATE_TO_NEWS)
                intent.removeExtra(DeepLinkKeys.KEY_NOTICE_ID_TO_EXPAND)
            }
        activity.addOnNewIntentListener(listener)
        onDispose { activity.removeOnNewIntentListener(listener) }
    }
}
